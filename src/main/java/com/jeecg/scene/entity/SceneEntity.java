package com.jeecg.scene.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 场景管理
 * @date 2018-12-29 13:04:32
 */
@Entity
@Table(name = "jform_scene", schema = "")
@SuppressWarnings("serial")
public class SceneEntity implements java.io.Serializable {
    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    private java.lang.String name;
    /**
     * 编码
     */
    @Excel(name = "编码", width = 15)
    private java.lang.String code;
    /**
     * 比例
     */
    @Excel(name = "比例", width = 15)
    private java.lang.Integer scale;
    /**
     * 展示类型
     */
    @Excel(name = "展示类型", width = 15, dicCode = "showType")
    private java.lang.String showType;
    /**
     * 2D场景
     */
    @Excel(name = "2D场景", width = 15)
    private java.lang.String scene2d;
    /**
     * 3D场景
     */
    @Excel(name = "3D场景", width = 15)
    private java.lang.String scene3d;
    /**
     * 位置
     */
    @Excel(name = "位置", width = 15)
    private java.lang.String address;
    /**
     * 坐标
     */
    @Excel(name = "坐标", width = 15)
    private java.lang.String position;
    /**
     * 坐标
     */
    @Excel(name = "3D信息", width = 15)
    private java.lang.String threeData;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15, dicCode = "isValid")
    private java.lang.Integer status;
    /**
     * 是否为默认
     */
    @Excel(name = "是否为默认", width = 15, dicCode = "isValid")
    private Boolean isDefaultView;
    /**
     * 是否自动旋转
     */
    @Excel(name = "是否自动旋转", width = 15, dicCode = "isValid")
    private Boolean isAutoRotate;
    /**
     * 设备关系
     */
    @Excel(name = "设备关系", width = 15, dicCode = "isValid")
    private Boolean isDeviceRelation;
    /**
     * 设备列表
     */
    @Excel(name = "设备列表", width = 15, dicCode = "isValid")
    private Boolean isDevicePanel;
    /**
     * 是否显示标签
     */
    @Excel(name = "是否显示标签", width = 15, dicCode = "isValid")
    private Boolean isLabelDisplay;
    /**
     * 性能控件
     */
    @Excel(name = "性能控件", width = 15, dicCode = "isValid")
    private Boolean isStatsDisplay;
    /**
     * 设备统计
     */
    @Excel(name = "设备统计", width = 15, dicCode = "isValid")
    private Boolean isDeviceStatus;
    /**
     * 是否显示阴影
     */
    @Excel(name = "是否显示阴影", width = 15, dicCode = "isValid")
    private Boolean isShadowDisplay;

    /**
     * 是否显示人员定位
     */
    @Excel(name = "是否人员定位", width = 15, dicCode = "isValid")
    private Boolean isUserPosition;
    /**
     * 创建人
     */
    @Excel(name = "创建人", width = 15)
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createDate;
    /**
     * 更新人
     */
    @Excel(name = "更新人", width = 15)
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date updateDate;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 主键
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
     * @param: java.lang.String 主键
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 名称
     */
    @Column(name = "NAME", nullable = false, length = 32)
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 名称
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 比例
     */
    @Column(name = "SCALE", nullable = false, length = 32)
    public java.lang.Integer getScale() {
        return this.scale;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer 比例
     */
    public void setScale(java.lang.Integer scale) {
        this.scale = scale;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 编码
     */
    @Column(name = "CODE", nullable = false, length = 32)
    public java.lang.String getCode() {
        return this.code;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 编码
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 展示类型
     */
    @Column(name = "SHOW_TYPE", nullable = false, length = 32)
    public java.lang.String getShowType() {
        return this.showType;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 展示类型
     */
    public void setShowType(java.lang.String showType) {
        this.showType = showType;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 2D场景
     */
    @Column(name = "SCENE_2D", nullable = true, length = 128)
    public java.lang.String getScene2d() {
        return this.scene2d;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 2D场景
     */
    public void setScene2d(java.lang.String scene2d) {
        this.scene2d = scene2d;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 3D场景
     */
    @Column(name = "SCENE_3D", nullable = true, length = 128)
    public java.lang.String getScene3d() {
        return this.scene3d;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 3D场景
     */
    public void setScene3d(java.lang.String scene3d) {
        this.scene3d = scene3d;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 位置
     */
    @Column(name = "ADDRESS", nullable = true, length = 50)
    public java.lang.String getAddress() {
        return this.address;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 位置
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 坐标
     */
    @Column(name = "POSITION", nullable = true, length = 32)
    public java.lang.String getPosition() {
        return this.position;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 坐标
     */
    public void setPosition(java.lang.String position) {
        this.position = position;
    }

    @Column(name = "THREE_DATA", nullable = true, length = 200)
    public java.lang.String getThreeData() {
        return threeData;
    }

    public void setThreeData(java.lang.String threeData) {
        this.threeData = threeData;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 状态
     */
    @Column(name = "STATUS", nullable = false, length = 32)
    public java.lang.Integer getStatus() {
        return this.status;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 状态
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    @Column(name = "IS_DEFAULT_VIEW", nullable = false, length = 5)
    public Boolean getIsDefaultView() {
        return isDefaultView;
    }

    public void setIsDefaultView(Boolean isDefaultView) {
        this.isDefaultView = isDefaultView;
    }

    @Column(name = "IS_AUTO_ROTATE", nullable = false, length = 5)
    public Boolean getIsAutoRotate() {
        return isAutoRotate;
    }

    public void setIsAutoRotate(Boolean isAutoRotate) {
        this.isAutoRotate = isAutoRotate;
    }

    @Column(name = "IS_DEVICE_RELATION", nullable = false, length = 5)
    public Boolean getIsDeviceRelation() {
        return isDeviceRelation;
    }

    public void setIsDeviceRelation(Boolean isDeviceRelation) {
        this.isDeviceRelation = isDeviceRelation;
    }

    @Column(name = "IS_DEVICE_PANEL", nullable = false, length = 5)
    public Boolean getIsDevicePanel() {
        return isDevicePanel;
    }

    public void setIsDevicePanel(Boolean isDevicePanel) {
        this.isDevicePanel = isDevicePanel;
    }

    @Column(name = "IS_LABEL_DISPLAY", nullable = false, length = 5)
    public Boolean getIsLabelDisplay() {
        return isLabelDisplay;
    }

    public void setIsLabelDisplay(Boolean isLabelDisplay) {
        this.isLabelDisplay = isLabelDisplay;
    }

    @Column(name = "IS_STATS_DISPLAY", nullable = false, length = 5)
    public Boolean getIsStatsDisplay() {
        return isStatsDisplay;
    }

    public void setIsStatsDisplay(Boolean isStatsDisplay) {
        this.isStatsDisplay = isStatsDisplay;
    }

    @Column(name = "IS_DEVICE_STATUS", nullable = false, length = 5)
    public Boolean getIsDeviceStatus() {
        return isDeviceStatus;
    }

    public void setIsDeviceStatus(Boolean isDeviceStatus) {
        this.isDeviceStatus = isDeviceStatus;
    }

    @Column(name = "IS_SHADOW_DISPLAY", nullable = false, length = 5)
    public Boolean getIsShadowDisplay() {
        return isShadowDisplay;
    }

    public void setIsShadowDisplay(Boolean isShadowDisplay) {
        this.isShadowDisplay = isShadowDisplay;
    }

    @Column(name = "IS_USER_POSITION", nullable = false, length = 5)
    public Boolean getIsUserPosition() {
        return isUserPosition;
    }

    public void setIsUserPosition(Boolean isUserPosition) {
        this.isUserPosition = isUserPosition;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人
     */

    @Column(name = "CREATE_BY", nullable = false, length = 36)
    public java.lang.String getCreateBy() {
        return this.createBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 创建人
     */
    public void setCreateBy(java.lang.String createBy) {
        this.createBy = createBy;
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
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 更新人
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 36)
    public java.lang.String getUpdateBy() {
        return this.updateBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 更新人
     */
    public void setUpdateBy(java.lang.String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true, length = 20)
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date 更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 备注
     */
    @Column(name = "REMARK", nullable = true, length = 128)
    public java.lang.String getRemark() {
        return this.remark;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 备注
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }
}