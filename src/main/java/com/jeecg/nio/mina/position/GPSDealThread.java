package com.jeecg.nio.mina.position;

import com.jeecg.nio.mqtt.service.MqttPublishClient;
import com.jeecg.nio.mqtt.service.MqttSendSevice;
import com.jeecg.position.entity.PositionEventEntity;
import com.jeecg.position.entity.PositionModuleConfigEntity;
import com.jeecg.position.entity.PositionModuleEntity;
import com.jeecg.position.entity.PositionTrajectoryEntity;
import com.jeecg.position.service.PositionModuleConfigServiceI;
import com.jeecg.position.service.PositionTrajectoryServiceI;
import com.jeecg.position.utils.FenceUtil;
import com.jeecg.position.utils.GPSUtil;
import net.sf.json.JSONObject;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.util.*;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @name: GPSDealThread
 * @description:
 * @author: Erudite Loong
 * @version: 1.0
 * @time: 2019/9/2 0002 14:59
 */
@Component
public class GPSDealThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GPSDealThread.class);

    @Autowired
    private SystemService systemService;
    @Autowired
    private PositionModuleConfigServiceI pmConfSevice;
    @Autowired
    private PositionTrajectoryServiceI pTrajectoryService;
    @Autowired
    private MqttSendSevice mqttSendSevice;

    private Object message;
    private IoSession session;
    private MqttPublishClient mqttPublish;

    public GPSDealThread() {
        super();
    }

    public GPSDealThread(IoSession session, Object message) {
        this.session = session;
        this.message = message;
        try {
            this.mqttPublish = MqttPublishClient.getInstance();
            this.systemService = ApplicationContextUtil.getContext().getBean(SystemService.class);
            this.pmConfSevice = ApplicationContextUtil.getContext().getBean(PositionModuleConfigServiceI.class);
            this.pTrajectoryService = ApplicationContextUtil.getContext().getBean(PositionTrajectoryServiceI.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 通过读取数据长度，判断功能
        JSONObject json = JSONObject.fromObject(this.message);
        String msg = json.getString("msg");
        int num = json.getInt("num");
        byte[] bMsg = HexConvertUtil.hexStringToBytes(msg);
        String imei = this.getImeiBySession(this.session);
        if (StringUtil.isBlank(imei)) {
            if (num != 0x01) {
                this.restart();
                return;
            }
        } else {
            saveOrUpdateModule(imei);
        }
        byte[] bNum = {(byte) num};
        this.mqttPublish.publishMessage(GPSServerHandler.class, "INFO", "imei: " + imei + ", num: " + HexConvertUtil.BinaryToHexString(bNum), "$forallcn/iotcp/logger");
        switch (num) {
            case 0x01: // 登录请求78 78 0A 01 01 23 45 67 89 01 23 45 01 0D 0A
                this.login(bMsg);
                break;
            case 0x08:
                // 心跳包: 起始位2byte 包长度1byte 协议号 1byte 结束位2byte; eg.7878 01 08 0D0A
                this.saveEvent(imei, "0x08", "心跳");
                manualPositionSend();
                break;
            case 0xBC:
            case 0x10:
                this.saveEvent(imei, "0x10", "GPS定位");
                logger.info("****************" + imei + ": GPS定位 *****************");
                this.GPSLocation(imei, bMsg);
                break;
            case 0xBD:
            case 0x11:
                this.saveEvent(imei, "0x11", "GPS离线定位");
                this.GPSLocation(imei, bMsg);
                break;
            case 0x13:
                this.saveEvent(imei, "0x13", "状态包");
                this.GPSStatePackage(imei, bMsg);
                break;
            case 0x14:
                this.saveEvent(imei, "0x14", "设备休眠");
                PositionModuleEntity pModule = new PositionModuleEntity();
                pModule.setId(imei);
                pModule.setUpdateDate(new Date());
                pModule.setOnlineStatus(2);
                systemService.saveOrUpdate(pModule);
                break;
            case 0x30:
                this.updateTime();
                this.saveEvent(imei, "0x30", "同步时间");
                break;
            case 0x57:
                this.saveEvent(imei, "0x57", "同步设置数据");
                PositionModuleConfigEntity pmConf = systemService.get(PositionModuleConfigEntity.class, imei);
                if (null == pmConf) {
                    try {
                        pmConfSevice.syncSettingData(session, pmConf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 0x17:
            case 0x69:
                this.lbsWifiLocation(imei, bMsg);
                this.saveEvent(imei, "0x17", "WIFI LBS定位");
                break;
            case 0x80:
                manualPosition(bMsg, imei);
                this.saveEvent(imei, "0x80", "手动定位");
                break;
            case 0x81:
                manualPositionSend();
                this.saveEvent(imei, "0x81", "充电完成");
                break;
            case 0x82:
                manualPositionSend();
                this.saveEvent(imei, "0x82", "充电连接");
                break;
            case 0x83:
                manualPositionSend();
                this.saveEvent(imei, "0x83", "充电断开");
                break;
            case 0x94:
                manualPositionSend();
                this.saveEvent(imei, "0x94", "震动报警");
                break;
            case 0x99:
                manualPositionSend();
                this.saveEvent(imei, "0x99", "SOS报警");
                break;
            case 0xb3:
                this.getSimIccid(bMsg, imei);
                this.saveEvent(imei, "0xb3", "获取iccid");
                break;
            case 0xb5:
                this.saveEvent(imei, "0xb5", "进入无网络连接省电模式");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + num);
        }
    }

    /**
     * 是否在场景边界内部
     *
     * @param point 经纬度
     * @return
     */
    private Boolean isInPoly(double[] point) {
        double[] xy = pTrajectoryService.lonlat2three(point[0], point[1]);
        String sql = "select p.point from position_fence f left join jform_scene s on f.scene_by=s.id left join position_fence_point p on p.fence_by=f.id " +
                "where 1=1 and s.is_default_view=1 and f.is_scene_boundary=1";
        List<Map<String, Object>> list = systemService.findForJdbc(sql);
        List<Point2D.Double> points = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String[] pArr = map.get("point").toString().split(",");
            Point2D.Double p = new Point2D.Double(Double.valueOf(pArr[0]), Double.valueOf(pArr[2]));
            points.add(p);
        }
        if (points.size() <= 0) new RuntimeException("围栏点集合为0");
        Boolean isInPoly = FenceUtil.contains(points, new Point2D.Double(xy[0], xy[1]));
        return isInPoly;
    }

    /**
     * 发送位置信息到前端首页
     *
     * @param imei
     * @param lonLat
     * @param isInPoly
     * @return
     */
    private Boolean sendPosition(String imei, double[] lonLat, long datetime, Boolean isInPoly) {
        double[] xy = pTrajectoryService.lonlat2three(lonLat[0], lonLat[1]);
        JSONObject three = new JSONObject();
        three.put("imei", imei);
        three.put("x", xy[0] + "");
        three.put("y", xy[1] + "");
        three.put("isInPoly", isInPoly);
        three.put("datetime", datetime);
        this.mqttPublish.publishMessage(this.getClass(), "error", "[mqtt] " + three, "$forallcn/iotcp/logger");
        this.mqttPublish.publishMessage("$forallcn/iotcp/position/location", three.toString(), 0);
        return isInPoly;
    }

    /**
     * 手动定位请求
     */
    private void manualPositionSend() {
        byte[] bmp = HexConvertUtil.hexStringToBytes("78 78 01 80 0D 0A");
        session.write(IoBuffer.wrap(bmp));
    }

    /**
     * 起始位2byte 数据包长度1byte 协议号 1byte 原因1byte 停止位 2byte Eg.78 78 02 80 xx 0d 0a
     *
     * @param msg
     */
    private void manualPosition(byte[] msg, String imei) {
        int b = msg[4] & 0xff;
        switch (b) {
            case 0x01:
                logger.info("定位卡[{}]手动定位原因：01-时间不正确", imei);
                break;
            case 0x02:
                logger.info("定位卡[{}]手动定位原因：02-lbs数量少", imei);
                break;
            case 0x03:
                logger.info("定位卡[{}]手动定位原因：03-wifi数量少", imei);
                break;
            case 0x04:
                logger.info("定位卡[{}]手动定位原因：04-lbs查找次数超过3次", imei);
                break;
            case 0x05:
                logger.info("定位卡[{}]手动定位原因：05-相同的lbs和wifi数据", imei);
                break;
            case 0x06:
                logger.info("定位卡[{}]手动定位原因：06-禁止lbs上传，同时没有wifi", imei);
                break;
            case 0x07:
                logger.info("定位卡[{}]手动定位原因： 07-gps间距小于50米", imei);
                break;
            default:
                break;
        }
    }

    /**
     * 模块登录
     *
     * @param msg
     */
    private void login(byte[] msg) {
        byte[] bImei = new byte[8];
        System.arraycopy(msg, 4, bImei, 0, 8);
        String imei = HexConvertUtil.BinaryToHexString(bImei).replaceAll(" ", "");
        // 去掉前面的0
        imei = imei.indexOf("0", 0) != -1 ? imei.substring(1, imei.length()) : imei;
        this.session.setAttribute("imei", imei);
        this.saveOrUpdateModule(imei);
        byte[] b1 = HexConvertUtil.hexStringToBytes("78 78 01 01 0D 0A");
        session.write(IoBuffer.wrap(b1));
        this.syncSetting(imei);
        this.saveEvent(imei, "0x01", "登录成功");
    }

    private void saveOrUpdateModule(String imei) {
        String ipPort = this.session.getRemoteAddress().toString();
        // 后台判断设备，设备存在且有效，返回登陆成功信息
        PositionModuleEntity module = systemService.get(PositionModuleEntity.class, imei);
        if (module == null) {
            this.saveModule(imei, ipPort);
        } else {
            this.updateModule(imei, ipPort);
        }
    }

    /**
     * 保存定位卡
     *
     * @param imei
     * @param ipPort
     */
    private void saveModule(String imei, String ipPort) {
        PositionModuleEntity pModule = new PositionModuleEntity();
        pModule.setId(imei);
        pModule.setIpPort(ipPort);
        pModule.setIsValid(1);
        pModule.setOnlineStatus(1);
        pModule.setCreateDate(new Date());
        pModule.setUpdateDate(new Date());
        systemService.save(pModule);
    }

    /**
     * 更新定位卡
     *
     * @param imei
     * @param ipPort
     */
    private void updateModule(String imei, String ipPort) {
        PositionModuleEntity pModule = new PositionModuleEntity();
        pModule.setIsValid(1);
        pModule.setIpPort(ipPort);
        pModule.setUpdateDate(new Date());
        pModule.setOnlineStatus(1);
        PositionModuleEntity t = systemService.get(PositionModuleEntity.class, imei);
        try {
            MyBeanUtils.copyBeanNotNull2Bean(pModule, t);
            systemService.saveOrUpdate(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * GPS状态包
     *
     * @param bMsg
     */
    private void GPSStatePackage(String imei, byte[] bMsg) {
        int electricity = bMsg[4] & 0xff; // 电量
        int version = bMsg[5] & 0xff; // 软件版本
        int timeZoom = bMsg[6] & 0xff; // 时区
        int aliveTime = bMsg[7] & 0xff; // 心跳上传间隔
        int signal = bMsg[8] & 0xff; // 信号
        PositionModuleEntity pModule = systemService.get(PositionModuleEntity.class, imei);
        if (pModule == null) {
            session.closeOnFlush();
        } else {
            pModule.setUpdateDate(new Date());
            pModule.setElectricity(electricity);
            pModule.setSignals(signal);
            pModule.setVersion(version);
            pModule.setAliveTime(aliveTime);
            systemService.updateEntitie(pModule);
        }
    }

    private String getWIFIParas(String imei, byte[] bWifiData, Date date) {
        int num = bWifiData.length / 7;
//        logger.info("{} wifi热点数量：{}", imei, num);
        StringBuffer wl = new StringBuffer();
        for (int i = 0; i < (num > 8 ? 8 : num); i++) {
            byte[] bBssid = new byte[6];
            System.arraycopy(bWifiData, i * 7, bBssid, 0, 6);
            String bssid = HexConvertUtil.BinaryToHexString(bBssid).replaceAll(" ", "");

            byte[] bRssi = new byte[1];
            int rssi = bWifiData[(6 + (i * 7))] & 0xff;
            wl.append(bssid.substring(0, bssid.length() - 1).replaceAll(" ", ":")).append(",").append(rssi).append(";");
        }
        return wl.toString().length() > 0 ? wl.toString().substring(0, wl.toString().length() - 1) : "";
    }

    private String getLBSParas(String imei, byte[] bLbsData, Date date) {
        int num = (bLbsData.length - 3) / 5;
//        logger.info("{} lbs基站数量：{}", imei, num);
        // MCCMNC：mcc 2byte，mnc 1byte 01CC00为46000
        int mcc = ((bLbsData[0] & 0xff) << 8) + (bLbsData[1] & 0xff);
        int mnc = bLbsData[2] & 0xff;
        StringBuffer cl = new StringBuffer();
        for (int i = 0; i < (num > 5 ? 5 : num); i++) {
            int index = 3 + (i * 5);
            int lac = ((bLbsData[index] & 0xff) << 8) + (bLbsData[index + 1] & 0xff);
            int cellid = ((bLbsData[index + 2] & 0xff) << 8) + (bLbsData[index + 3] & 0xff);

            int mciss = bLbsData[index + 4] & 0xff;
            cl.append(mcc).append(",").append(mnc).append(",").append(lac).append(",").append(cellid).append(",").append(0 - mciss).append(";");
        }
        return cl.toString().length() > 0 ? cl.toString().substring(0, cl.toString().length() - 1) : "";
    }

    /**
     * GPS定位包
     *
     * @param bMsg
     */
    private void GPSLocation(String imei, byte[] bMsg) {
        /**
         * 起始位2byte 数据包长度1byte 协议号1byte 日期时间 6byte GPS 数据长度，可见卫星个数1byte GPS经纬度 8byte 速度
         * 1byte 东西经南北纬 状态 航向2byte 停止位2byte eg.7878 12 10 0A03170F3217 9C 026B3F3E
         * 0C22AD65 1F 3460 0D0A
         */
        // GPS数据长度，可见卫星个数：数据长度和可见卫星数各占0.5byte
        byte[] bGpsLen = new byte[1];
        System.arraycopy(bMsg, 10, bGpsLen, 0, 1);
        int gpsDataLen = (bGpsLen[0] >> 4) & 0x0F; // GPS数据长度
        int satelliteLen = bGpsLen[0] & 0x0F; // 可见卫星个数
        // 经纬度
        byte[] bLatitude = new byte[4];
        System.arraycopy(bMsg, 11, bLatitude, 0, 4);
        long latitude = ((bLatitude[0] & 0xFF) << 24) + ((bLatitude[1] & 0xFF) << 16) + ((bLatitude[2] & 0xFF) << 8) + (bLatitude[3] & 0xFF);
        double lat = latitude / 30000d;
        lat = ((int) (lat / 60)) + ((lat % 60) / 60);

        byte[] bLongitude = new byte[4];
        System.arraycopy(bMsg, 15, bLongitude, 0, 4);
        long longitude = ((bLongitude[0] & 0xFF) << 24) + ((bLongitude[1] & 0xFF) << 16) + ((bLongitude[2] & 0xFF) << 8) + (bLongitude[3] & 0xFF);
        double lon = longitude / 30000d;
        lon = ((int) (lon / 60)) + ((lon % 60) / 60);

        // GPS速度：占用1byte
        byte[] bGpsSpeed = new byte[1];
        System.arraycopy(bMsg, 19, bGpsSpeed, 0, 1);
        int gpsSpeed = bGpsSpeed[0] & 0xFF;

        // GPS速度：占用1byte
        byte[] bOther = new byte[2];
        System.arraycopy(bMsg, 20, bOther, 0, 2);
        int other = bGpsLen[0] & 0xFF;
        // 航向
        float course = ((bOther[0] & 0x03) << 4) + bOther[1] & 0xff;

        // GPS是否定位
        int isLocate = (bOther[0] >> 4) & 0x01;

        // 东西经
        int ewLongitude = (bOther[0] >> 3) & 0x01;

        // 南北纬
        int nsLatitude = (bOther[0] >> 2) & 0x01;

        // 日期时间 6byte
        byte[] bDatetime10 = new byte[6];
        System.arraycopy(bMsg, 4, bDatetime10, 0, 6);
        String datetime10 = HexConvertUtil.BinaryToHexString(bDatetime10);
        byte[] bBack = HexConvertUtil.hexStringToBytes("78 78 00 10 " + datetime10 + " 0D 0A");
        bBack[3] = bMsg[3];
        session.write(IoBuffer.wrap(bBack));
        Date date = formatDateByHex(bDatetime10, "dec");
        this.saveTrajectoryData(lon, lat, imei, date, isLocate);
    }

    /**
     * lbswifi综合定位
     *
     * @param imei
     * @param bMsg
     */
    private void lbsWifiLocation(String imei, byte[] bMsg) {
        int locateType = 0;
        // 日期时间 6byte
        byte[] bDatetime69 = new byte[6];
        System.arraycopy(bMsg, 4, bDatetime69, 0, 6);
        String datetime69 = HexConvertUtil.BinaryToHexString(bDatetime69);
        byte[] bBack = HexConvertUtil.hexStringToBytes("78 78 00 69 " + datetime69 + " 0D 0A");
        bBack[3] = bMsg[3];
        session.write(IoBuffer.wrap(bBack));
        Date date = formatDateByHex(bDatetime69, "hex");

        String cl = "", wl = "";
        // wifi长度
        int wifiSize = bMsg[2] & 0xff;
        if (wifiSize > 0) {
            locateType += 0x01 << 1;
            byte[] bWifiData = new byte[wifiSize * 7];
            System.arraycopy(bMsg, 10, bWifiData, 0, wifiSize * 7);
            wl = this.getWIFIParas(imei, bWifiData, date);
        }

        // lbs长度
        int lbeIndex = 2 + 1 + 1 + 6 + (wifiSize * 7); // 2byte起始位 + 1byteWiFi长度 + 1byte协议号 + 6byte时间 + WiFi
        int lbsSize = bMsg[lbeIndex] & 0xff;
        if (lbsSize > 0) {
            locateType += 0x01 << 2;
            int len = 3 + (lbsSize * 5);
            byte[] bLbsData = new byte[len];
            System.arraycopy(bMsg, lbeIndex + 1, bLbsData, 0, len);
            cl = this.getLBSParas(imei, bLbsData, date);
        }

        // 混合定位
        String url = " http://api.cellocation.com:81/loc/";
        String parem = "wl=" + wl.toLowerCase() + "&cl=" + cl + "&output=json";
        com.alibaba.fastjson.JSONObject reqJson = HttpRequest.sendGet(url, parem);
        if ((reqJson != null) && (reqJson.getInteger("errcode") == 0)) {
            // 保存数据
            double lon = reqJson.getDouble("lon") != null ? reqJson.getDouble("lon") : 0;
            double lat = reqJson.getDouble("lat") != null ? reqJson.getDouble("lat") : 0;
            this.saveTrajectoryData(lon, lat, imei, date, locateType);
        } else {
            logger.error("LBS|WIFI定位失败：{}", reqJson != null ? reqJson.getString("errcode") : "");
        }
    }

    /**
     * eg.7878 07 30 07E00705053718 0D0A 时间为16进制
     */
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-0"));
        String sDate = sdf.format(Calendar.getInstance().getTime());
        String[] arrDate = sDate.split(" ");
        byte[] bCmd = {0x78, 0x78, 0x07, 0x30, (byte) ((Integer.valueOf(arrDate[0]) & 0xff00) >> 8), (byte) (Integer.valueOf(arrDate[0]) & 0x00ff),
                (byte) (Integer.valueOf(arrDate[1]) & 0xff), (byte) (Integer.valueOf(arrDate[2]) & 0xff), (byte) (Integer.valueOf(arrDate[3]) & 0xff),
                (byte) (Integer.valueOf(arrDate[4]) & 0xff), (byte) (Integer.valueOf(arrDate[5]) & 0xff), 0x0D, 0x0A};
//        logger.info("同步时间：{}", bCmd);
        this.session.write(IoBuffer.wrap(bCmd));
    }

    /**
     * 同步设置
     *
     * @param imei
     */
    public void syncSetting(String imei) {
        PositionModuleConfigEntity pmConf = systemService.get(PositionModuleConfigEntity.class, imei);
        try {
            this.updateTime();
            if (null == pmConf) {
                pmConf = pmConfSevice.saveDefConf(imei);
                // 同步设置数据
                pmConfSevice.syncSettingData(this.session, pmConf);
                Thread.sleep(1000);
                // 设置定位方式
                pmConfSevice.setPositionMode(this.session, pmConf.getPositionMode());
                Thread.sleep(1000);
                // 飘逸机制
                pmConfSevice.setDriftMode(session, pmConf.getDriftMode());
                Thread.sleep(1000);
                restart();
                Thread.sleep(1000);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd HH mm ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-0"));
        String sDate = sdf.format(Calendar.getInstance().getTime());
        String[] arrDate = sDate.split(" ");
        System.out.println(sDate);
        byte[] bCmd = {0x78, 0x78, 0x07, 0x30, (byte) ((Integer.valueOf(arrDate[0]) & 0xff00) >> 8), (byte) (Integer.valueOf(arrDate[0]) & 0x00ff),
                (byte) (Integer.valueOf(arrDate[1]) & 0xff), (byte) (Integer.valueOf(arrDate[2]) & 0xff), (byte) (Integer.valueOf(arrDate[3]) & 0xff),
                (byte) (Integer.valueOf(arrDate[4]) & 0xff), (byte) (Integer.valueOf(arrDate[5]) & 0xff), 0x0D, 0x0A};
        logger.info("同步时间：{}", HexConvertUtil.BinaryToHexString(bCmd));
        System.out.println(new Date());

        System.out.println((byte) ((2019 & 0xff00) >> 8));

        byte[] b = {0x16, 0x04, 0x13, 0x03, 0x18, 0x49};
        System.out.println(HexConvertUtil.BinaryToHexString(b));
    }

    /**
     * 获取设备sim卡iccid 起始位2byte 包长度1byte 协议号1byte iccid（ascii码） 20byte 结束位2byte
     * Eg.7878 15 b3 3839383630343035313931383732313132363739 0d0a ->
     * 89860405191872112679
     *
     * @param bMsg
     * @param imei
     */
    private void getSimIccid(byte[] bMsg, String imei) {
        byte[] bIccid = new byte[20];
        System.arraycopy(bMsg, 4, bIccid, 0, 20);
        String iccid = HexConvertUtil.BinaryToHexString(bIccid).replaceAll(" ", "");
        PositionModuleEntity pm = systemService.get(PositionModuleEntity.class, imei);
        pm.setIccid(iccid);
        systemService.updateEntitie(pm);
    }

    private void saveTrajectoryData(double lon, double lat, String imei, Date date, Integer locateType) {
        PositionTrajectoryEntity pTrajectory = new PositionTrajectoryEntity();
        pTrajectory.setCreateDate(date);
        pTrajectory.setImei(imei);
        double[] a = GPSUtil.gps84_To_Gcj02(lon, lat);
        pTrajectory.setLatitude(a[1]);
        pTrajectory.setLongitude(a[0]);
        pTrajectory.setLocateType(locateType);
        Boolean isInPoly = this.isInPoly(a);
        // 一分钟内的离线数据可以发送
        if (new Date().getTime() - date.getTime() < 1000 * 60) {
            this.sendPosition(imei, a, date.getTime(), isInPoly);
        }
        pTrajectory.setIsCross(isInPoly ? 0 : 1);
        systemService.save(pTrajectory);
    }

    /**
     * 保存事件
     *
     * @param imet
     * @param code
     * @param name
     */
    private void saveEvent(String imet, String code, String name) {
        PositionEventEntity event = new PositionEventEntity();
        event.setImei(imet);
        event.setCode(code);
        event.setName(name);
        event.setCreateDate(new Date());
        event.setUpdateDate(new Date());
        systemService.save(event);
    }

    /**
     * 格式化日期
     *
     * @param hex  时间byte
     * @param flag hex | dec
     * @return
     */
    private Date formatDateByHex(byte[] hex, String flag) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("y MM dd HH mm ss ");
        try {
            if ("hex".equals(flag)) {
                date = sdf.parse(HexConvertUtil.BinaryToHexString(hex));
            } else {
                date = sdf.parse(HexConvertUtil.BinaryToDecString(hex));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 加8个时区
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
        date = calendar.getTime();
//        logger.info("时间：{}", DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));
        return date;
    }

    /**
     * 从IOSession中获取imei
     *
     * @param session
     * @return 有返回imei，没有返回null
     */
    private String getImeiBySession(IoSession session) {
        Object imeiObj = session.getAttribute("imei");
        if (imeiObj != null) {
            return imeiObj.toString();
        }
        return null;
    }

    private void restart() {
        this.mqttPublish.publishMessage(GPSDealThread.class, "INFO", "<<<<<<<<<<<<<<<<<<<<<<<<<<< restart >>>>>>>>>>>>>>>>>>>>>>>>", "$forallcn/iotcp/logger");
        byte[] b = HexConvertUtil.hexStringToBytes("78 78 01 48 01 0D 0A");
        session.write(IoBuffer.wrap(b));
    }
}
