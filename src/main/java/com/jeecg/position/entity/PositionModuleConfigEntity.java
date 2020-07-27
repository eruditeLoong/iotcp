package com.jeecg.position.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 定位卡设置
 * @author onlineGenerator
 * @date 2019-09-06 16:26:24
 * @version V1.0
 *
 */
@Entity
@Table(name = "position_module_config", schema = "")
@SuppressWarnings("serial")
public class PositionModuleConfigEntity implements java.io.Serializable {
    /** 主键 */
    private java.lang.String id;
    /** IMEI */
    @Excel(name = "IMEI", width = 15)
    private java.lang.String imei;
    /** 定位模式 */
    @Excel(name = "定位模式", width = 15)
    private java.lang.Integer positionMode;
    /** 飘逸机制 */
    @Excel(name = "飘逸机制", width = 15)
    private java.lang.Integer driftMode;
    /** 自动定位时间s */
    @Excel(name = "自动定位时间s", width = 15)
    private java.lang.Integer autoLocalTime;
    /** 是否休眠 */
    @Excel(name = "是否休眠", width = 15)
    private java.lang.Boolean isSleep;
    /** 是否休眠 */
    @Excel(name = "是否震动", width = 15)
    private java.lang.Boolean isShock;
    /** 震动报警 */
    @Excel(name = "震动报警", width = 15)
    private java.lang.Boolean isShockAlarm;
    /** 位移报警 */
    @Excel(name = "位移报警", width = 15)
    private java.lang.Boolean isMoveAlarm;
    /** 低电报警 */
    @Excel(name = "低电报警", width = 15)
    private java.lang.Boolean isLowPowerAlarm;
    /** 围栏报警 */
    @Excel(name = "围栏报警", width = 15)
    private java.lang.Boolean isFenceAlarm;
    /** SOS报警 */
    @Excel(name = "SOS报警", width = 15)
    private java.lang.Boolean isSosAlarm;
    /** 出省报警 */
    @Excel(name = "出省报警", width = 15)
    private java.lang.Boolean isOutTownAlarm;
    /** 分离报警 */
    @Excel(name = "分离报警", width = 15)
    private java.lang.Boolean isSeparationAlarm;
    /** 风险报警 */
    @Excel(name = "风险报警", width = 15)
    private java.lang.Boolean isRiskAlarm;
    /** 休眠时段 */
    @Excel(name = "休眠时段", width = 15)
    private java.lang.String sleepTime;
    /** 震动等级 */
    @Excel(name = "震动等级", width = 15)
    private java.lang.Integer shockLevel;
    @Excel(name = "关机或重启", width = 15)
    private java.lang.Integer restart;

    @Column(name = "RESTART", nullable = true, length = 1)
    public java.lang.Integer getRestart() {
        return restart;
    }

    public void setRestart(java.lang.Integer restart) {
        this.restart = restart;
    }

    /** 更新时间 */
    @Excel(name = "更新时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateDate;

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 主键
     */
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")

    @Column(name = "ID", nullable = false, length = 36)
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 主键
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String IMEI
     */

    @Column(name = "IMEI", nullable = false, length = 36)
    public java.lang.String getImei() {
        return this.imei;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String IMEI
     */
    public void setImei(java.lang.String imei) {
        this.imei = imei;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 定位模式
     */
    @Column(name = "POSITION_MODE", nullable = false, length = 1)
    public java.lang.Integer getPositionMode() {
        return this.positionMode;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer 定位模式
     */
    public void setPositionMode(java.lang.Integer positionMode) {
        this.positionMode = positionMode;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 飘逸机制
     */
    @Column(name = "DRIFT_MODE", nullable = false, length = 1)
    public java.lang.Integer getDriftMode() {
        return this.driftMode;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer 飘逸机制
     */
    public void setDriftMode(java.lang.Integer driftMode) {
        this.driftMode = driftMode;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 自动定位时间s
     */

    @Column(name = "AUTO_LOCAL_TIME", nullable = false, length = 5)
    public java.lang.Integer getAutoLocalTime() {
        return this.autoLocalTime;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 自动定位时间s
     */
    public void setAutoLocalTime(java.lang.Integer autoLocalTime) {
        this.autoLocalTime = autoLocalTime;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 是否休眠
     */

    @Column(name = "IS_SLEEP", nullable = true, length = 1)
    public java.lang.Boolean getIsSleep() {
        return this.isSleep;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 是否休眠
     */
    public void setIsSleep(java.lang.Boolean isSleep) {
        this.isSleep = isSleep;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 是否震动
     */

    @Column(name = "IS_SHOCK", nullable = true, length = 1)
    public java.lang.Boolean getIsShock() {
        return isShock;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 是否震动
     */
    public void setIsShock(java.lang.Boolean isShock) {
        this.isShock = isShock;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 震动报警
     */

    @Column(name = "IS_SHOCK_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsShockAlarm() {
        return this.isShockAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 震动报警
     */
    public void setIsShockAlarm(java.lang.Boolean isShockAlarm) {
        this.isShockAlarm = isShockAlarm;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 位移报警
     */

    @Column(name = "IS_MOVE_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsMoveAlarm() {
        return this.isMoveAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 位移报警
     */
    public void setIsMoveAlarm(java.lang.Boolean isMoveAlarm) {
        this.isMoveAlarm = isMoveAlarm;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 低电报警
     */

    @Column(name = "IS_LOW_POWER_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsLowPowerAlarm() {
        return this.isLowPowerAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 低电报警
     */
    public void setIsLowPowerAlarm(java.lang.Boolean isLowPowerAlarm) {
        this.isLowPowerAlarm = isLowPowerAlarm;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 围栏报警
     */

    @Column(name = "IS_FENCE_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsFenceAlarm() {
        return this.isFenceAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 围栏报警
     */
    public void setIsFenceAlarm(java.lang.Boolean isFenceAlarm) {
        this.isFenceAlarm = isFenceAlarm;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean SOS报警
     */

    @Column(name = "IS_SOS_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsSosAlarm() {
        return this.isSosAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean SOS报警
     */
    public void setIsSosAlarm(java.lang.Boolean isSosAlarm) {
        this.isSosAlarm = isSosAlarm;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 出省报警
     */

    @Column(name = "IS_OUT_TOWN_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsOutTownAlarm() {
        return this.isOutTownAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 出省报警
     */
    public void setIsOutTownAlarm(java.lang.Boolean isOutTownAlarm) {
        this.isOutTownAlarm = isOutTownAlarm;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 分离报警
     */

    @Column(name = "IS_SEPARATION_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsSeparationAlarm() {
        return this.isSeparationAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 分离报警
     */
    public void setIsSeparationAlarm(java.lang.Boolean isSeparationAlarm) {
        this.isSeparationAlarm = isSeparationAlarm;
    }

    /**
     * 方法: 取得java.lang.Boolean
     * 
     * @return: java.lang.Boolean 风险报警
     */

    @Column(name = "IS_RISK_ALARM", nullable = true, length = 1)
    public java.lang.Boolean getIsRiskAlarm() {
        return this.isRiskAlarm;
    }

    /**
     * 方法: 设置java.lang.Boolean
     * 
     * @param: java.lang.Boolean 风险报警
     */
    public void setIsRiskAlarm(java.lang.Boolean isRiskAlarm) {
        this.isRiskAlarm = isRiskAlarm;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 休眠时段
     */

    @Column(name = "SLEEP_TIME", nullable = true, length = 32)
    public java.lang.String getSleepTime() {
        return this.sleepTime;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 休眠时段
     */
    public void setSleepTime(java.lang.String sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 震动等级
     */

    @Column(name = "SHOCK_LEVEL", nullable = false, length = 3)
    public java.lang.Integer getShockLevel() {
        return this.shockLevel;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 震动等级
     */
    public void setShockLevel(java.lang.Integer shockLevel) {
        this.shockLevel = shockLevel;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新时间
     */

    @Column(name = "UPDATE_DATE", nullable = false, length = 32)
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 更新时间
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "PositionModuleConfigEntity [id=" + id + ", imei=" + imei + ", positionMode=" + positionMode + ", autoLocalTime=" + autoLocalTime + ", isSleep="
                + isSleep + ", isShock=" + isShock + ", isShockAlarm=" + isShockAlarm + ", isMoveAlarm=" + isMoveAlarm + ", isLowPowerAlarm=" + isLowPowerAlarm
                + ", isFenceAlarm=" + isFenceAlarm + ", isSosAlarm=" + isSosAlarm + ", isOutTownAlarm=" + isOutTownAlarm + ", isSeparationAlarm="
                + isSeparationAlarm + ", isRiskAlarm=" + isRiskAlarm + ", sleepTime=" + sleepTime + ", shockLevel=" + shockLevel + ", updateDate=" + updateDate
                + "]";
    }
}