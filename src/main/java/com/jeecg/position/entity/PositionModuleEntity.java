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
 * @Description: 定位模块信息
 * @author onlineGenerator
 * @date 2019-09-05 12:43:50
 * @version V1.0
 *
 */
@Entity
@Table(name = "position_module", schema = "")
@SuppressWarnings("serial")
public class PositionModuleEntity implements java.io.Serializable {
    /** 主键 */
    private java.lang.String id;
    /** 卡号 */
    @Excel(name = "卡号", width = 15)
    private java.lang.String cardNo;
    /** ICCID */
    @Excel(name = "ICCID", width = 15)
    private java.lang.String iccid;
    /** 地址 */
    @Excel(name = "IP_PORT", width = 15)
    private java.lang.String ipPort;
    /** 硬件协议 */
    @Excel(name = "硬件协议", width = 15, dicCode = "isValid")
    private java.lang.String protocol;
    /** 电量 */
    @Excel(name = "电量", width = 15)
    private java.lang.Integer electricity;
    /** 信号 */
    @Excel(name = "信号", width = 15)
    private java.lang.Integer signals;
    /** 软件版本 */
    @Excel(name = "软件版本", width = 15)
    private java.lang.Integer version;
    /** 时区 */
    @Excel(name = "时区", width = 15)
    private java.lang.Integer timeZoom;
    /** 心跳间隔 */
    @Excel(name = "心跳间隔", width = 15)
    private java.lang.Integer aliveTime;
    /** 在线状态 */
    @Excel(name = "在线状态", width = 15, dicCode = "isValid")
    private java.lang.Integer onlineStatus;
    /** 是否有效 */
    @Excel(name = "是否有效", width = 15, dicCode = "isValid")
    private java.lang.Integer isValid;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新时间 */
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
     * @return: java.lang.String 卡号
     */

    @Column(name = "CARD_NO", nullable = true, length = 12)
    public java.lang.String getCardNo() {
        return this.cardNo;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 卡号
     */
    public void setCardNo(java.lang.String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String ICCID
     */

    @Column(name = "ICCID", nullable = true, length = 25)
    public java.lang.String getIccid() {
        return this.iccid;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String ICCID
     */
    public void setIccid(java.lang.String iccid) {
        this.iccid = iccid;
    }

    @Column(name = "IP_PORT", nullable = true, length = 36)
    public java.lang.String getIpPort() {
        return ipPort;
    }

    public void setIpPort(java.lang.String ipPort) {
        this.ipPort = ipPort;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 硬件协议
     */

    @Column(name = "PROTOCOL", nullable = true, length = 36)
    public java.lang.String getProtocol() {
        return this.protocol;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 硬件协议
     */
    public void setProtocol(java.lang.String protocol) {
        this.protocol = protocol;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 电量
     */

    @Column(name = "ELECTRICITY", nullable = true, length = 3)
    public java.lang.Integer getElectricity() {
        return this.electricity;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 电量
     */
    public void setElectricity(java.lang.Integer electricity) {
        this.electricity = electricity;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 信号
     */

    @Column(name = "SIGNALS", nullable = true, length = 3)
    public java.lang.Integer getSignals() {
        return this.signals;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 信号
     */
    public void setSignals(java.lang.Integer signals) {
        this.signals = signals;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 软件版本
     */

    @Column(name = "VERSION", nullable = true, length = 3)
    public java.lang.Integer getVersion() {
        return this.version;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 软件版本
     */
    public void setVersion(java.lang.Integer version) {
        this.version = version;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 时区
     */

    @Column(name = "TIME_ZOOM", nullable = true, length = 3)
    public java.lang.Integer getTimeZoom() {
        return this.timeZoom;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 时区
     */
    public void setTimeZoom(java.lang.Integer timeZoom) {
        this.timeZoom = timeZoom;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 心跳间隔
     */

    @Column(name = "ALIVE_TIME", nullable = true, length = 3)
    public java.lang.Integer getAliveTime() {
        return this.aliveTime;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 心跳间隔
     */
    public void setAliveTime(java.lang.Integer aliveTime) {
        this.aliveTime = aliveTime;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 在线状态
     */

    @Column(name = "ONLINE_STATUS", nullable = true, length = 1)
    public java.lang.Integer getOnlineStatus() {
        return this.onlineStatus;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 在线状态
     */
    public void setOnlineStatus(java.lang.Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 是否有效
     */

    @Column(name = "IS_VALID", nullable = false, length = 1)
    public java.lang.Integer getIsValid() {
        return this.isValid;
    }

    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer 是否有效
     */
    public void setIsValid(java.lang.Integer isValid) {
        this.isValid = isValid;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */

    @Column(name = "CREATE_DATE", nullable = false, length = 20)
    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date 创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新时间
     */

    @Column(name = "UPDATE_DATE", nullable = true, length = 20)
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
}