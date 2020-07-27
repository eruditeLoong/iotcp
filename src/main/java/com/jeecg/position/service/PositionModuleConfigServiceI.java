package com.jeecg.position.service;

import com.jeecg.position.entity.PositionModuleConfigEntity;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PositionModuleConfigServiceI extends CommonService {

    public void delete(PositionModuleConfigEntity entity) throws Exception;

    public Serializable save(PositionModuleConfigEntity entity) throws Exception;

    public void saveOrUpdate(PositionModuleConfigEntity entity) throws Exception;

    public PositionModuleConfigEntity saveDefConf(String id) throws Exception;

    /**
     * 获取mina连接session
     * 
     * @param imei
     * @return
     */
    public IoSession getIoSession(String imei);

    /**
     * 获取session总数
     * @return
     */
    public Integer getIoSessionTotal();

    /**
     * 设置模块配置信息
     * 
     * @param conf
     */
    public void setModuleConf(PositionModuleConfigEntity conf) throws Exception;

    /**
     * 设置定位方式
     * 
     * @param session
     * @param mode
     */
    public void setPositionMode(IoSession session, Integer mode);

    void resetIoSession();

    /**
     * 设置飘逸机制
     * @param session
     * @param elegantMode
     */
    void setDriftMode(IoSession session, Integer elegantMode);

    /**
     * 设置定位数据上传间隔
     * 
     * @param session
     * @param time    为0时停止上传数据
     */
    public void setUpdateTime(IoSession session, Integer time);

    /**
     * 震动报警
     * 
     * @param session
     * @param isShock true:开
     */
    public void setShockAlarm(IoSession session, Boolean isShock);

    /**
     * 同步设置数据
     * 
     * @param conf
     * @throws Exception
     */
    public void syncSettingData(IoSession session, PositionModuleConfigEntity conf) throws Exception;

    /**
     * 找设备
     * 
     * @param session
     * @param flag    1:开始；0：结束
     */
    public void searchModule(IoSession session, int flag);

    /**
     * 重启设备
     * 
     * @param session
     * @param flag    1:重启；2：关机
     */
    public void restartModule(IoSession session, int flag);

    /**
     * 设置休眠
     * 
     * @param session
     * @param conf
     */
    void setSleep(IoSession session, PositionModuleConfigEntity conf);

    /**
     * 修改服务器地址
     * @param session
     * @param service
     */
    public void confService(IoSession session, String service);
}
