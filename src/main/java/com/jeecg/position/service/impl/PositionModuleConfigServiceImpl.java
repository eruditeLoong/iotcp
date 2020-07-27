package com.jeecg.position.service.impl;

import com.jeecg.nio.mina.position.GPSServerHandler;
import com.jeecg.position.controller.PositionModuleConfigController;
import com.jeecg.position.entity.PositionModuleConfigEntity;
import com.jeecg.position.entity.PositionModuleEntity;
import com.jeecg.position.service.PositionModuleConfigServiceI;
import com.jeecg.position.service.PositionModuleServiceI;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.HexConvertUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("positionModuleConfigService")
@Transactional
public class PositionModuleConfigServiceImpl extends CommonServiceImpl implements PositionModuleConfigServiceI {
    private static final Logger logger = LoggerFactory.getLogger(PositionModuleConfigController.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private PositionModuleServiceI positionModuleService;
    @Autowired
    private SystemService systemService;

    public void delete(PositionModuleConfigEntity entity) throws Exception {
        super.delete(entity);
    }

    public Serializable save(PositionModuleConfigEntity entity) throws Exception {
        Serializable t = super.save(entity);
        return t;
    }

    public void saveOrUpdate(PositionModuleConfigEntity entity) throws Exception {
        super.saveOrUpdate(entity);
    }

    @Override
    public PositionModuleConfigEntity saveDefConf(String id) throws Exception {
        PositionModuleConfigEntity pmConf = new PositionModuleConfigEntity();
        pmConf.setId(id);
        pmConf.setImei(id);
        pmConf.setAutoLocalTime(120); // 上传间隔：2分钟
        pmConf.setPositionMode(1); // 定位模式
        pmConf.setDriftMode(0);     // 飘逸机制
        pmConf.setIsShock(true); // 是否震动
        pmConf.setIsShockAlarm(true); // 震动报警
        pmConf.setShockLevel(99); // 震动等级
        pmConf.setIsSleep(false); // 是否休眠
        pmConf.setSleepTime(null); // 休眠时段
        pmConf.setIsLowPowerAlarm(true); // 低电报警
        pmConf.setIsMoveAlarm(true); // 位移报警
        pmConf.setIsFenceAlarm(true); // 围栏报警
        pmConf.setIsOutTownAlarm(true); // 出省报警
        pmConf.setIsRiskAlarm(true); // 风险报警
        pmConf.setIsSeparationAlarm(true); // 分离报警
        pmConf.setIsSosAlarm(true); // SOS报警
        pmConf.setUpdateDate(new Date());
        PositionModuleConfigEntity e = systemService.get(PositionModuleConfigEntity.class, id);
        if(e == null){
            super.save(pmConf);
        }else{
            MyBeanUtils.copyBeanNotNull2Bean(pmConf, e);
            super.saveOrUpdate(e);
        }
        return pmConf;
    }

    @Override
    public void setModuleConf(PositionModuleConfigEntity conf) throws Exception {
        try {
            PositionModuleEntity pModule = systemService.get(PositionModuleEntity.class,conf.getId());
            if(pModule!=null){
                IoSession session = this.getIoSession(pModule.getIpPort());
                if (session == null) {
                    throw new RuntimeException("*************** IoSession is null *************");
                }
                this.syncSettingData(session, conf);
                Thread.sleep(1000);
                // 设置定位方式
                this.setPositionMode(session, conf.getPositionMode());
                Thread.sleep(1000);
                // 飘逸机制
                this.setDriftMode(session, conf.getDriftMode());
                Thread.sleep(1000);
                // 重启
                this.restartModule(session, conf.getRestart());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void setPositionMode(IoSession session, Integer mode) {
        byte[] bMode = HexConvertUtil.hexStringToBytes("78 78 02 B0 01 0D 0A");
        bMode[4] = Byte.valueOf(mode.toString());
        session.write(IoBuffer.wrap(bMode));
    }

    @Override
    public Integer getIoSessionTotal(){
        ConcurrentHashMap<String, IoSession> sMap = GPSServerHandler.sessionsConcurrentHashMap;
        return sMap.size();
    }

    @Override
    public IoSession getIoSession(String addr) {
        ConcurrentHashMap<String, IoSession> sMap = GPSServerHandler.sessionsConcurrentHashMap;
        if(StringUtil.isNotBlank(addr)){
            return sMap.get(addr.split(":")[0]);
        }else{
            return null;
        }
    }

    @Override
    public void resetIoSession(){
        ConcurrentHashMap<String, IoSession> sMap = GPSServerHandler.sessionsConcurrentHashMap;
        Set<String> set = sMap.keySet();
        for (String addr: set){
            if(!sMap.get(addr).isConnected()){
                logger.info("移除无效IoSession：{}", addr);
                sMap.remove(addr);
            }
        }
    }

    @Override
    public void setDriftMode(IoSession session, Integer elegantMode) {
        byte[] bElegantMode = {0x78, 0x78, 0x01, (byte) 0xb9, 0x00, 0x0d, 0x0a};
        bElegantMode[5] = (byte) (elegantMode & 0xff);
        session.write(IoBuffer.wrap(bElegantMode));
    }

    @Override
    public void setUpdateTime(IoSession session, Integer time) {
        if (time > 0) {
            byte[] bTime = {0x78, 0x78, 0x03, (byte) 0x97, 0x00, 0x00, 0x0D, 0x0A};
            bTime[4] = (byte) (time.byteValue() & 0xff00);
            bTime[5] = (byte) (time.byteValue() & 0x00ff);
            session.write(IoBuffer.wrap(bTime));
        } else {
            byte[] bStop = {0x78, 0x78, 0x01, 0x44, 0x0D, 0x0A};
            session.write(IoBuffer.wrap(bStop));
        }
    }

    @Override
    public void setShockAlarm(IoSession session, Boolean isShock) {
        byte[] bShockOn = {0x78, 0x78, 0x01, (byte) 0x92, 0x01, 0x0D, 0x0A};
        byte[] bShockOff = {0x78, 0x78, 0x01, (byte) 0x93, 0x0D, 0x0A};
        session.write(IoBuffer.wrap(isShock ? bShockOn : bShockOff));
    }

    @Override
    public void setSleep(IoSession session, PositionModuleConfigEntity conf) {
        String sCmd = "78 78 05 46 00 00 00 00 00 0D 0A";
        byte[] bCmd = HexConvertUtil.hexStringToBytes(sCmd);
        // 睡眠开关1byte
        if (conf.getIsSleep() != null && conf.getIsSleep()) {
            bCmd[4] = 0x01;
        }
        // GPS定时时间4byte
        if (StringUtil.isNotBlank(conf.getSleepTime())) {
            String[] sleepTimes = conf.getSleepTime().split("-");
            String[] st1 = sleepTimes[0].split(":");
            String[] st2 = sleepTimes[1].split(":");
            bCmd[5] = Integer.valueOf(st1[0]).byteValue();
            bCmd[6] = Integer.valueOf(st1[1]).byteValue();
            bCmd[7] = Integer.valueOf(st2[0]).byteValue();
            bCmd[8] = Integer.valueOf(st2[1]).byteValue();
        }
        session.write(IoBuffer.wrap(bCmd));
    }

    /**
     * 同步设置数据
     */
    @Override
    public void syncSettingData(IoSession session, PositionModuleConfigEntity conf) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("78 78 "); // 起始位2byte[0 1]
        sb.append("00 "); // 包长度1byte[2]
        sb.append("57 "); // 协议号1byte[3]
        sb.append("00 60 "); // 上传间隔2byte[4 5]
        sb.append("00 "); // 开关1byte[6]
        sb.append("00 00 00 00 00 00 00 00 00 "); // 闹钟9byte[7 8 9 10 11 12 13 14 15]
        sb.append("00 "); // 勿扰时间开关1byte[16]
        sb.append("00 00 00 00 00 00 00 00 00 "); // 勿扰时间：星期1byte 起始时间1 1byte 结束时间1 2byte 起始时间2 1byte 结束时间2 2byte [17 18 19 20 21
        // 22 23
        // 24 25]
        sb.append("00 00 00 00 00 "); // GPS定时时间段： 开关 1byte 起始时间2 结束时间2 [26 27 28 29 30]
        sb.append("3B 3B 3B 0D 0A ");
        byte[] bCmd = HexConvertUtil.hexStringToBytes(sb.toString());
        // 数据长度2
        bCmd[2] = Integer.valueOf(bCmd.length - 5).byteValue();
        // 上传间隔2byte(4,5)，十进制：0060，这个为60秒
        bCmd[4] = (byte) (conf.getAutoLocalTime() & 0xff00);
        bCmd[5] = (byte) (conf.getAutoLocalTime() & 0x00ff);
        // 开关 1byte[6]
        // 开关-GPS
        if (conf.getPositionMode() > 0)
            bCmd[6] |= 0x01;
        // 开关-震动报警
        if (conf.getIsShockAlarm() != null && conf.getIsShockAlarm())
            bCmd[6] |= 0x04;

        // 睡眠开关1byte[16]
        if (conf.getIsSleep() != null && conf.getIsSleep()) {
            bCmd[16] = 0x01;
            // 勿扰时间, 星期1byte 起始时间1 2byte 结束时间1 2byte 起始时间2 2byte 结束时间2 2byte
            if (StringUtil.isNotBlank(conf.getSleepTime())) {
                String[] sleepTimes = conf.getSleepTime().split("-");
                String[] st1 = sleepTimes[0].split(":");
                String[] st2 = sleepTimes[1].split(":");
                bCmd[18] = Integer.valueOf(st1[0]).byteValue();
                bCmd[19] = Integer.valueOf(st1[1]).byteValue();
                bCmd[20] = Integer.valueOf(st2[0]).byteValue();
                bCmd[21] = Integer.valueOf(st2[1]).byteValue();

                // GPS定时时间4byte[]
                bCmd[27] = Integer.valueOf(st1[0]).byteValue();
                bCmd[28] = Integer.valueOf(st1[1]).byteValue();
                bCmd[29] = Integer.valueOf(st2[0]).byteValue();
                bCmd[30] = Integer.valueOf(st2[1]).byteValue();
            }
            // GPS 定时关闭-开启
            bCmd[26] = 0x01;
        } else {
            bCmd[16] = 0x00;
            // GPS 定时关闭-关闭
            bCmd[26] = 0x00;
        }
        session.write(IoBuffer.wrap(bCmd));
    }

    @Override
    public void searchModule(IoSession session, int flag) {
        byte[] bCmd = HexConvertUtil.hexStringToBytes("78 78 01 49 01 0D 0A");
        bCmd[4] = (byte) (flag & 0xff);
        session.write(IoBuffer.wrap(bCmd));
    }

    @Override
    public void restartModule(IoSession session, int flag) {
        byte[] bCmd = HexConvertUtil.hexStringToBytes("78 78 01 48 01 0D 0A");
        bCmd[4] = (byte) (flag == 1 ? 0x01 : 0x02);
        logger.info("*************** " + ((flag == 1) ? "重启设备 " : "设备关机 ") + HexConvertUtil.BinaryToHexString(bCmd) + " ******************");
        session.write(IoBuffer.wrap(bCmd));
    }

    @Override
    public void confService(IoSession session, String service) {
        String regex = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}\\#\\d{4}\\#";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(service);
        if(!mat.matches()){
            throw new RuntimeException("服务器地址格式错误！");
        }
        String s = HexConvertUtil.convertStringToHex(service, true);
        StringBuffer sbCmd = new StringBuffer("78 78 00 B1");
        sbCmd.append(s);
        sbCmd.append("0D 0A");
        byte[] bCmd = HexConvertUtil.hexStringToBytes(sbCmd.toString());
        // 数据长度2
        bCmd[2] = Integer.valueOf(bCmd.length - 6).byteValue();
        session.write(IoBuffer.wrap(bCmd));
    }

    public static void main(String[] args) {
        // Eg.787813b13132302e32352e3234332e38332336373839230D0A
        String regex = "\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}\\#\\d{4}\\#";
        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher("120.25.243.83#67889#");
        System.out.println(mat.matches());
        String s = HexConvertUtil.convertStringToHex("120.25.243.83#6789#", true);
        System.out.println(s);
        StringBuffer sbCmd = new StringBuffer("78 78 00 B1");
        sbCmd.append(s);
        sbCmd.append("0D 0A");
        byte[] bCmd = HexConvertUtil.hexStringToBytes(sbCmd.toString());
        // 数据长度2
        bCmd[2] = Integer.valueOf(bCmd.length - 6).byteValue();
    }
}