package com.jeecg.position.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 定位数据
 * @date 2019-09-06 15:31:09
 */
@Entity
@Table(name = "position_trajectory", schema = "")
@SuppressWarnings("serial")
public class PositionTrajectoryEntity implements java.io.Serializable {
    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * IMEI
     */
    @Excel(name = "IMEI", width = 15)
    private java.lang.String imei;
    /**
     * 纬度
     */
    @Excel(name = "纬度", width = 15)
    private java.lang.Double latitude;
    /**
     * 经度
     */
    @Excel(name = "经度", width = 15)
    private java.lang.Double longitude;
    /**
     * GPS速度
     */
    @Excel(name = "GPS速度", width = 15)
    private java.lang.Integer gpsSpeed;
    /**
     * 航向
     */
    @Excel(name = "航向", width = 15)
    private java.lang.Integer course;
    /**
     * 定位方式
     */
    @Excel(name = "定位方式", width = 15)
    private java.lang.Integer locateType;
    /**
     * 东西经
     */
    @Excel(name = "东西经", width = 15)
    private java.lang.Integer ewLongitude;
    /**
     * 南北纬
     */
    @Excel(name = "南北纬", width = 15)
    private java.lang.Integer nsLatitude;
    /**
     * 南北纬
     */
    @Excel(name = "是否越界", width = 15)
    private java.lang.Integer isCross;
    /**
     * 创建日期
     */
    private java.util.Date createDate;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  主键
     */
    @Id
    @GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

    @Column(name = "ID", nullable = false, length = 36)
    public java.lang.String getId() {
        return this.id;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  主键
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /*
     * 计算两点之间距离
     * @param start
     * @param end
     * @return 米
     */
    public double getDistance(PositionTrajectoryEntity point) {
        double lon1 = (Math.PI / 180) * this.longitude;
        double lon2 = (Math.PI / 180) * point.longitude;
        double lat1 = (Math.PI / 180) * this.latitude;
        double lat2 = (Math.PI / 180) * point.latitude;
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d * 1000;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  IMEI
     */

    @Column(name = "IMEI", nullable = false, length = 36)
    public java.lang.String getImei() {
        return this.imei;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  IMEI
     */
    public void setImei(java.lang.String imei) {
        this.imei = imei;
    }

    /**
     * 方法: 取得java.lang.Double
     *
     * @return: java.lang.Double  纬度
     */

    @Column(name = "LATITUDE", nullable = false, length = 32)
    public java.lang.Double getLatitude() {
        return this.latitude;
    }

    /**
     * 方法: 设置java.lang.Double
     *
     * @param: java.lang.Double  纬度
     */
    public void setLatitude(java.lang.Double latitude) {
        this.latitude = latitude;
    }

    /**
     * 方法: 取得java.lang.Double
     *
     * @return: java.lang.Double  经度
     */

    @Column(name = "LONGITUDE", nullable = false, length = 32)
    public java.lang.Double getLongitude() {
        return this.longitude;
    }

    /**
     * 方法: 设置java.lang.Double
     *
     * @param: java.lang.Double  经度
     */
    public void setLongitude(java.lang.Double longitude) {
        this.longitude = longitude;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  GPS速度
     */

    @Column(name = "GPS_SPEED", nullable = true, scale = 2, length = 3)
    public java.lang.Integer getGpsSpeed() {
        return this.gpsSpeed;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  GPS速度
     */
    public void setGpsSpeed(java.lang.Integer gpsSpeed) {
        this.gpsSpeed = gpsSpeed;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  航向
     */

    @Column(name = "COURSE", nullable = true, scale = 2, length = 3)
    public java.lang.Integer getCourse() {
        return this.course;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  航向
     */
    public void setCourse(java.lang.Integer course) {
        this.course = course;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  定位方式
     */

    @Column(name = "LOCATE_TYPE", nullable = true, length = 1)
    public java.lang.Integer getLocateType() {
        return this.locateType;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  定位方式
     */
    public void setLocateType(java.lang.Integer locateType) {
        this.locateType = locateType;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  东西经
     */

    @Column(name = "EW_LONGITUDE", nullable = true, length = 1)
    public java.lang.Integer getEwLongitude() {
        return this.ewLongitude;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  东西经
     */
    public void setEwLongitude(java.lang.Integer ewLongitude) {
        this.ewLongitude = ewLongitude;
    }

    @Column(name = "IS_CROSS", nullable = true, length = 1)
    public Integer getIsCross() {
        return isCross;
    }

    public void setIsCross(Integer isCross) {
        this.isCross = isCross;
    }
    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  南北纬
     */

    @Column(name = "NS_LATITUDE", nullable = true, length = 1)
    public java.lang.Integer getNsLatitude() {
        return this.nsLatitude;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  南北纬
     */
    public void setNsLatitude(java.lang.Integer nsLatitude) {
        this.nsLatitude = nsLatitude;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */

    @Column(name = "CREATE_DATE", nullable = false, length = 20)
    public java.util.Date getCreateDate() {
        return this.createDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
}