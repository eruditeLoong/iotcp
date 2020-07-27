package com.jeecg.nio.mina;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.HexConvertUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.TuiSongMsgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.jeecg.data.entity.IotAlarmDataEntity;
import com.jeecg.data.entity.IotDataEntity;
import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.nio.mqtt.service.MqttPublishClient;
import com.jeecg.protocol.entity.DataProtocolEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class MinaDataDealThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(MinaDataDealThread.class);

    @Autowired
    private SystemService systemService;

    private MqttPublishClient mqttPublish;
    private String address;
    private Object message;

    public MinaDataDealThread() {
        super();
    }

    public MinaDataDealThread(String address, Object message) {
        this.address = address;
        this.message = message;
        try {
            this.systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
            this.mqttPublish = MqttPublishClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void run() {

        String ip = StringUtil.getMatcher("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}", this.address);
        // 获取部署设备，根据默认场景下的部署设备IP地址
        String sql = "select sdd.* from jform_scene_device_depoly sdd left join jform_scene s on sdd.scene_by=s.id where 1=1 and s.is_default_view=1 and sdd.device_address=?";
        Map<String, Object> sddMap = systemService.findOneForJdbc(sql, ip);
        if (sddMap == null || sddMap.size() <= 0) {
            logger.info("IP地址解析有误：1、默认场景是否正确；2、客户端IP地址是否正确");
            this.mqttPublish.publishMessage(this.getClass(), "debug", "[mqtt]IP地址解析有误：1、默认场景是否正确；2、客户端IP地址是否正确；", "$forallcn/iotcp/logger");
            return;
        }
        this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]部署设备id: " + sddMap.get("id"), "$forallcn/iotcp/logger");
//		MqttUtil.send(this.getClass(), "$forallcn/iotcp/logger", "[mqtt]部署设备id: " + sddMap.get("id"));
        // 基础设备
        BaseDeviceEntity bDevice = systemService.get(BaseDeviceEntity.class, sddMap.get("device_by").toString());
        this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]部署设备: " + bDevice.getName(), "$forallcn/iotcp/logger");
        // 基础设备的数据格式
        DataProtocolEntity dpe = systemService.get(DataProtocolEntity.class, bDevice.getDataFormat());
        /**
         * TODO zhouwr 2019/08/11 22:51 我的构思：
         * 下一步将上边的逻辑封装后，在项目启动时执行，将所有数据放入cache或Redis中，这里只需读取里面的数据即可
         */
        // type == 0 固有格式
        Object obj = new Object();
        if ("0".equals(dpe.getType())) {
            switch (dpe.getCode()) {
            case "TH11S":
                obj = DecoderUtil.TH11SDecoder((byte[]) this.message);
                break;
            case "WIRELESS":
                obj = DecoderUtil.WIRELESSDecoder((byte[]) this.message);
                break;
            case "WIRELESS120":
                obj = DecoderUtil.WIRELESS120Decoder((byte[]) this.message);
                break;
            default:
                break;
            }
        } else {
            // 1、转换数据
            String data = this.getData(dpe.getDataShape());
            // 2、解析数据

        }
        if (obj != null) {
            this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]转换后数据：" + obj.toString(), "$forallcn/iotcp/logger");
            // 判断当前设备下是否还有子网关设备

            // 3、储存数据
            this.save(sddMap.get("id").toString(), bDevice.getType(), obj);
        }
    }

    /**
     * 回调函数：保存多级网关数据
     * 
     * @param deployId       当前网关部署ID
     * @param baseDeviceType 当前设备类型
     * @param obj            数据对象
     */
    private void save(String deployId, String baseDeviceType, Object obj) {
        // 判断是json数组，还是对象
        if (obj instanceof JSONArray) {
            // 数组：循环存储数据
            JSONArray jsonList = JSONArray.fromObject(obj);
            for (Object object : jsonList) {
                JSONObject json = JSONObject.fromObject(object);
                if (json.has("childData")) { // 有子集数据
                    deployId = getDeployDevice(baseDeviceType, deployId, json.getString("code"));
                    this.save(deployId, baseDeviceType, json.get("childData"));
                } else
                    saveJson(json, deployId, baseDeviceType);
            }
        } else if (obj instanceof JSONObject) {
            // 对象：存储数据一次
            JSONObject json = JSONObject.fromObject(obj); // 将字符串数据转化成JSON对象
            if (json.has("childData")) { // 有子集数据
                deployId = getDeployDevice(baseDeviceType, deployId, json.getString("code"));
                this.save(deployId, baseDeviceType, json.get("childData"));
            } else
                saveJson(json, deployId, baseDeviceType);
        } else {
        }
    }

    private void saveJson(JSONObject json, String deployId, String baseDeviceType) {
        String deployBy = getDeployDevice(baseDeviceType, deployId, json.getString("code"));
        if (StringUtil.isBlank(deployBy)) {
            this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]找不到部署设备：" + json.getString("code") + "，请检查部署设备是否存在！", "$forallcn/iotcp/logger");
            return;
        }
        // 再次获得基础设备
        SceneDeviceDepolyEntity sdd = systemService.get(SceneDeviceDepolyEntity.class, deployBy);
        BaseDeviceEntity bDevice = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
        // 设备数据点 集合
        List<BaseDeviceDataEntity> bddList = systemService.findByProperty(BaseDeviceDataEntity.class, "deviceBy", bDevice.getId());
        this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]基础设备: " + bDevice.getName(), "$forallcn/iotcp/logger");
        for (int i = 0; i < bddList.size(); i++) {
            BaseDeviceDataEntity bdd = bddList.get(i);
            if (json.has(bdd.getField())) {
                int dv = dataValidate(json.getString(bdd.getField()), bdd.getUnit(), bdd.getNormalDataRange());
                IotDataEntity data = saveIotData(bdd, deployBy, json, dv);
                if (1 != dv) {
                    sendMsg(bDevice.getName() + json.getString("code"), bdd.getName(), json.getString(bdd.getField()), bdd.getUnit(), bdd.getNormalDataRange());
                    saveAlarmData(data, bDevice, sdd, bdd.getNormalDataRange());
                }
                /* 发送数据到首页 */
                mqttSendData(data, dv != 1 ? true : false);
            } else {
                String msg = "设备：" + bDevice.getName() + json.getString("code") + "数据节点：" + bdd.getName() + "[" + bdd.getField() + "]未找到！";
                logger.info(msg);
                this.mqttPublish.publishMessage(this.getClass(), "debug", msg, "$forallcn/iotcp/logger");
            }
        }
    }

    /**
     * 保持报警信息到数据库
     * 
     * @param data
     * @param bDevice
     * @param sdd
     * @param normalDataRange
     */
    private void saveAlarmData(IotDataEntity data, BaseDeviceEntity bDevice, SceneDeviceDepolyEntity sdd, String normalDataRange) {
        /* 录入报警信息 */
        IotAlarmDataEntity alarmData = new IotAlarmDataEntity();
        alarmData.setAlarmDate(data.getCreateDate()); // 报警时间，直接引用数据创建时间
        alarmData.setAlarmLevel(0);
        alarmData.setBaseDeviceBy(bDevice.getId());
        alarmData.setBaseDeviceName(bDevice.getName());
        alarmData.setDataLabel(data.getLabel());
        alarmData.setDataField(data.getFieldBy());
        alarmData.setDataValue(data.getData());
        alarmData.setSceneBy(sdd.getSceneBy());
        alarmData.setDealStatus(0);
        alarmData.setDeployDeviceBy(data.getInstanceDeviceBy());
        alarmData.setDeployDeviceCode(sdd.getDeviceCode());
        alarmData.setNormalDataRange(normalDataRange);
        systemService.save(alarmData);
    }

    /**
     * 保存物联数据
     * 
     * @param bdd
     * @param deployBy
     * @param json
     * @param status
     * @return
     */
    private IotDataEntity saveIotData(BaseDeviceDataEntity bdd, String deployBy, JSONObject json, Integer status) {
        IotDataEntity data = new IotDataEntity();
        data.setLabel(bdd.getName());
        data.setFieldBy(bdd.getField());
        data.setInstanceDeviceBy(deployBy);
        data.setData(json.getString(bdd.getField()));
        data.setType(bdd.getDataType());
        data.setCreateDate(new Date());
        data.setDirection(1);
        data.setStatus(status);
        systemService.save(data);
        return data;
    }

    /**
     * mqtt发送数据到前端展示
     * 
     * @param data
     * @param isAlarm
     */
    private void mqttSendData(IotDataEntity data, Boolean isAlarm) {
        JSONObject iotData = new JSONObject();
        iotData.put("id", data.getInstanceDeviceBy());
        iotData.put("isAlarm", isAlarm);
        JSONObject iotVal = new JSONObject();
        iotVal.put("field", data.getFieldBy());
        iotVal.put("value", data.getData());
        iotData.put("data", iotVal);
        this.mqttPublish.publishMessage("$forallcn/iotcp/iotData", JSON.toJSONString(iotData), 0);
    }

    /**
     * 数据验证
     *
     * @param data
     * @param unit
     * @param dataRange
     * @return 1：正常；0：报警
     */
    private int dataValidate(Object data, String unit, String dataRange) {
        // 数据节点的正常数据范围
        String drs[] = dataRange.split(",");
        switch (unit) {
        case "string":
            break;
        case "int":
            break;
        case "float":
            break;
        case "double":
            break;
        case "bool":
            break;
        case "byte":
            break;
        case "enum":
            break;
        }
        if (Float.valueOf(data.toString()) < Float.valueOf(drs[0])) {
            return 0;
        } else if (Float.valueOf(data.toString()) > Float.valueOf(drs[1])) {
            return 0;
        } else {
        }
        return 1;
    }

    /**
     * 温度传感器2的温度数值不在正常数值范围内，请注意！正常数值范围：30-60摄氏度，当前温度：80℃。 deviceName:"温度传感器3",
     * label:"温度", dataRange:"20-80", unit:"℃", value:"90"
     */
    private void sendMsg(String deviceName, String label, Object data, String unit, String dataRange) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("deviceName", deviceName);
        msg.put("label", label);
        msg.put("dataRange", dataRange);
        msg.put("unit", unit);
        msg.put("value", data);
        // mqtt发送报警信息，
        this.mqttPublish.publishMessage("$forallcn/iotcp/alarm", TuiSongMsgUtil.getMessageContent("IOT_DATA_ALARM", msg), 0);
        TuiSongMsgUtil.sendMessage("IOT_DATA_ALARM", msg, "admin", "admin");// 发送消息
    }

    /**
     * 得到部署设备，如果是终端设备直接返回部署id，如果是网关设备，则获取子设备的id
     *
     * @param baseDeviceType 设备类型
     * @param deployId       设备部署id
     * @param code           数据中的设备code
     * @return
     */
    private String getDeployDevice(String baseDeviceType, String deployId, String code) {
//		logger.info(deployId +", "+ baseDeviceType +", "+ code);
        // 判断基础设备类型
        if ("terminal".equals(baseDeviceType)) {
            // 终端：直接使用deployId
            return deployId;
        } else if ("gateway".equals(baseDeviceType)) {
            // 网关：需要根据网关的deployId，找到所有的子设备，使用子设备的部署id
            String sql = "select sdd.* from jform_scene_device_depoly sdd left join jform_scene s on sdd.scene_by=s.id where 1=1 and s.is_default_view=1 and sdd.device_parent_by=? and device_code=?";
            Map<String, Object> sddMap = systemService.findOneForJdbc(sql, deployId, code);
            if (sddMap != null && sddMap.size() >= 1) {
                return sddMap.get("id").toString();
            }
        } else {
        }
        return "";
    }

    /**
     * 根据数据类型，转换数据
     *
     * @param dataType
     * @return
     */
    private String getData(String dataType) {
        String data = "";
        switch (dataType) {
        case "string":
            try {
                data = new String((byte[]) this.message, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            break;
        case "byte":
            data = HexConvertUtil.BinaryToHexString((byte[]) this.message);
            break;
        case "bit":
            try {
                data = new String((byte[]) this.message, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            break;
        default:
            break;
        }
        return data;
    }
}