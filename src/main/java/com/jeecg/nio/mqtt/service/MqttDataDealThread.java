package com.jeecg.nio.mqtt.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.GroupDefinitionException;

import com.jeecg.device.entity.BaseDeviceEntity;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecg.data.entity.IotDataEntity;
import com.jeecg.data.service.IotDataServiceI;
import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.protocol.entity.DataCustomEntity;
import com.jeecg.protocol.entity.DataProtocolEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.service.SceneDeviceDepolyServiceI;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class MqttDataDealThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(MqttDataDealThread.class);

	@Autowired
	private SystemService systemService;
	@Autowired
	private IotDataServiceI iotDataService;
	@Autowired
	private SceneDeviceDepolyServiceI sceneDeviceDepolyService;

	private String topic;
	private MqttMessage message;

	public MqttDataDealThread() {
		super();
	}

	public MqttDataDealThread(String topic, MqttMessage message) {
		this.topic = topic;
		this.message = message;
		System.out.println("MqttDataDealThread***");
	}

	public synchronized void run() {
		try {
			sceneDeviceDepolyService = ApplicationContextUtil.getContext().getBean(SceneDeviceDepolyServiceI.class);
			systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
			if (this.topic.indexOf("$forallcn/iotcp/") != -1) {
				// 截取设备部署id
				String deviceDeployId = this.topic.substring(this.topic.lastIndexOf("/") + 1, this.topic.length());
				System.out.println("部署设备id>>>" + deviceDeployId);
				SceneDeviceDepolyEntity sdd = systemService.get(SceneDeviceDepolyEntity.class, deviceDeployId);
				// 基础设备
				BaseDeviceEntity bDevice = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
				// 基础设备的数据格式
				DataProtocolEntity dpe = systemService.get(DataProtocolEntity.class, bDevice.getDataFormat());
				// 判断数据格式, 如果是网关设备：终端设备的数据格式采用网关设备
				if ("JSON".equals(dpe.getCode())) {
					// 判断是json数据还是对象
					String msg = new String(message.getPayload());
					dealJsonData(msg, sdd.getDeviceBy(), bDevice.getType(), deviceDeployId);
				} else if ("OTHER_CUSTOM".equals(dpe.getCode())) {
					dealCustomData(message.getPayload(), dpe);
				} else {
					dealCustomData(message.getPayload(), dpe);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	public SceneDeviceDepolyEntity getChildDeviceDeployId(String deviceDeployId, String deviceCode) throws Exception {
		System.out.println("网关设备提交数据");
		// 查询子级设备，根据deviceParentBy和code
		// deviceCode 需要从接收数据中提取
		List<SceneDeviceDepolyEntity> childList = systemService.findHql("from SceneDeviceDepolyEntity where deviceParentBy=? and deviceCode=?", deviceDeployId, deviceCode);
		if (childList != null && childList.size() > 0) {
			return childList.get(0);
		} else {
			throw new Exception("未找到子设备设备：" + deviceDeployId);
		}
	}

	/**
	 * 处理json数据
	 * 
	 * @param msg
	 * @param devId
	 * @param deviceDeployId
	 */
	public void dealJsonData(String msg, String deviceBy, String deviceType, String sceneDeviceDeployBy) {
		String mathReg = "[\\[{].+[}\\]]";
		msg = StringUtil.getMatcher(mathReg, msg);
		String type = StringUtil.getJsonType(msg);
		try {
			switch (type) {
			case "JSONArray":
				JSONArray jsonList = JSONArray.fromObject(msg);
				for (Object object : jsonList) {
					JSONObject json = JSONObject.fromObject(object);
					savaJsonObjData(json, sceneDeviceDeployBy, deviceBy, deviceType);
				}
				break;
			case "JSONObject":
				JSONObject json = JSONObject.fromObject(msg); // 将字符串数据转化成JSON对象
				savaJsonObjData(json, sceneDeviceDeployBy, deviceBy, deviceType);
				break;
			default:
				logger.debug("JSON数据格式不合法！{}", msg);
				new GroupDefinitionException("JSON数据格式不合法！");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理自定义数据
	 * 
	 * @param msg
	 */
	public void dealCustomData(byte[] msg, DataProtocolEntity dpe) {
		String hql1 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? order by node_index asc";
		List<DataCustomEntity> customList = systemService.findHql(hql1, dpe.getId());
		String dataShape = dpe.getDataShape(); // 传输数据的形式：string、byte
		for (DataCustomEntity custom : customList) {
			String lable = custom.getNodeName();
			String[] index = custom.getNodeIndex().split(",");
			String field = custom.getNodeField();
			String datatype = custom.getDatatype(); // 基本数据类型
			IotDataEntity data = new IotDataEntity();
			data.setInstanceDeviceBy(lable);
			data.setFieldBy(field);
			data.setType(datatype);
			data.setStatus(1);
			data.setDirection(0); // 0：接收；1：发送
			if ("string".equals(dataShape)) {
				String msgStr = new String(msg);
				data.setData(msgStr.substring(Integer.valueOf(index[0]), Integer.valueOf(index[1])));
			} else if ("byte".equals(dataShape)) {
				int len = Integer.valueOf(index[1]) - Integer.valueOf(index[0]);
				byte[] b = new byte[len];
				System.arraycopy(msg, Integer.valueOf(index[0]), b, 0, len);
				data.setData(new String(b));
			} else {

			}
			systemService.save(data);
		}
	}

	public void savaJsonObjData(JSONObject json, String sceneDeviceDeployBy, String deviceBy, String deviceType) {
		String deviceCode = json.getString("code");
		if ("gateway".equals(deviceType)) { // 网关设备，需要获取子设备
			SceneDeviceDepolyEntity childSdd;
			try {
				childSdd = getChildDeviceDeployId(sceneDeviceDeployBy, deviceCode);
				sceneDeviceDeployBy = childSdd.getId(); // 子设备的部署id
				deviceBy = childSdd.getDeviceBy(); // 子设备的id
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 查询-基础设备数据点
		String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
		List<BaseDeviceDataEntity> baseDeviceDataList = systemService.findHql(hql0, deviceBy);
		IotDataEntity data = new IotDataEntity();
		for (BaseDeviceDataEntity bdDate : baseDeviceDataList) {
			String lable = bdDate.getName();
			String field = bdDate.getField();
			String datatype = bdDate.getDataType();
			if ("enum".equals(bdDate.getDataType())) {

			}
			// 判断设备上传的json中有无key：field
			if (json.has(field)) {
				String value = json.getString(field);
				System.out.println(field + "：" + value);
				data.setData(value);
				data.setInstanceDeviceBy(sceneDeviceDeployBy);
				data.setLabel(lable);
				data.setFieldBy(field);
				data.setType(datatype);
				try {
					Integer status = iotDataService.getDataStatus(data);
					data.setStatus(status);
				} catch (Exception e) {
					e.printStackTrace();
				}
				data.setDirection(0); // 0：接收；1：发送
				data.setCreateDate(new Date());
				systemService.save(data);
			} else {
				System.out.println("找不到json关键字");
			}
		}
	}

	public void savaJsonObjData(JSONObject json, String baseDeviceId) {
		// 查询-基础设备数据点
		String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
		List<BaseDeviceDataEntity> baseDeviceDataList = systemService.findHql(hql0, baseDeviceId);
		IotDataEntity data = new IotDataEntity();
		for (BaseDeviceDataEntity bdDate : baseDeviceDataList) {
			String lable = bdDate.getName();
			String field = bdDate.getField();
			String datatype = bdDate.getDataType();
			if ("enum".equals(bdDate.getDataType())) {

			}
			String field_ = json.has(field) ? json.getString(field) : "";
			// 判断设备上传的field和软件设置的是否一致
			if (field.equals(field_)) {
				data.setInstanceDeviceBy(lable);
				data.setFieldBy(field_);
				data.setData(json.has("value") ? json.getString("value") : "");
				data.setType(datatype);
				try {
					Integer status = iotDataService.getDataStatus(data);
					data.setStatus(status);
				} catch (Exception e) {
					e.printStackTrace();
				}
				data.setDirection(0); // 0：接收；1：发送
			} else {
				new GroupDefinitionException("设备上传的field和软件设置的不一致！");
			}
		}
		try {
			systemService.save(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}