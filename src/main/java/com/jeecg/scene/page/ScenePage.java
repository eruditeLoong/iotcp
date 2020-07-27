package com.jeecg.scene.page;

import com.jeecg.position.entity.PositionUserEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @name: ScenePage
 * @description:
 * @author: Erudite Loong
 * @version: 1.0
 * @time: 2019/9/26 0026 11:09
 */
public class ScenePage implements java.io.Serializable{

    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * 名称
     */
    private java.lang.String name;
    /**
     * 编码
     */
    private java.lang.String code;
    /**
     * 比例
     */
    private java.lang.Integer scale;
    /**
     * 展示类型
     */
    private java.lang.String showType;
    /**
     * 2D场景
     */
    private java.lang.String scene2d;
    /**
     * 3D场景
     */
    private java.lang.String scene3d;
    /**
     * 位置
     */
    private java.lang.String address;
    /**
     * 坐标
     */
    private java.lang.String position;
    /**
     * 坐标
     */
    private java.lang.String threeData;
    /**
     * 状态
     */
    private java.lang.String status;
    /**
     * 是否为默认
     */
    private Boolean isDefaultView;
    /**
     * 是否自动旋转
     */
    private Boolean isAutoRotate;
    /**
     * 设备关系
     */
    private Boolean isDeviceRelation;
    /**
     * 设备列表
     */
    private Boolean isDevicePanel;
    /**
     * 是否显示标签
     */
    private Boolean isLabelDisplay;
    /**
     * 性能控件
     */
    private Boolean isStatsDisplay;
    /**
     * 设备统计
     */
    private Boolean isDeviceStatus;
    /**
     * 是否显示阴影
     */
    private Boolean isShadowDisplay;

    /**
     * 是否显示人员定位
     */
    private Boolean isUserPosition;
    /**
     * 创建人
     */
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人
     */
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 备注
     */
    private java.lang.String remark;

    private List<SceneDeviceDepolyEntity> deployDevices = new ArrayList<>();
    private List<PositionUserEntity> positionUsers = new ArrayList<>();


    public List<SceneDeviceDepolyEntity> getDeployDevices() {
        return deployDevices;
    }

    public void setDeployDevices(List<SceneDeviceDepolyEntity> deployDevices) {
        this.deployDevices = deployDevices;
    }

    public List<PositionUserEntity> getPositionUsers() {
        return positionUsers;
    }

    public void setPositionUsers(List<PositionUserEntity> positionUsers) {
        this.positionUsers = positionUsers;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getScene2d() {
        return scene2d;
    }

    public void setScene2d(String scene2d) {
        this.scene2d = scene2d;
    }

    public String getScene3d() {
        return scene3d;
    }

    public void setScene3d(String scene3d) {
        this.scene3d = scene3d;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getThreeData() {
        return threeData;
    }

    public void setThreeData(String threeData) {
        this.threeData = threeData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDefaultView() {
        return isDefaultView;
    }

    public void setDefaultView(Boolean defaultView) {
        isDefaultView = defaultView;
    }

    public Boolean getAutoRotate() {
        return isAutoRotate;
    }

    public void setAutoRotate(Boolean autoRotate) {
        isAutoRotate = autoRotate;
    }

    public Boolean getDeviceRelation() {
        return isDeviceRelation;
    }

    public void setDeviceRelation(Boolean deviceRelation) {
        isDeviceRelation = deviceRelation;
    }

    public Boolean getDevicePanel() {
        return isDevicePanel;
    }

    public void setDevicePanel(Boolean devicePanel) {
        isDevicePanel = devicePanel;
    }

    public Boolean getLabelDisplay() {
        return isLabelDisplay;
    }

    public void setLabelDisplay(Boolean labelDisplay) {
        isLabelDisplay = labelDisplay;
    }

    public Boolean getStatsDisplay() {
        return isStatsDisplay;
    }

    public void setStatsDisplay(Boolean statsDisplay) {
        isStatsDisplay = statsDisplay;
    }

    public Boolean getDeviceStatus() {
        return isDeviceStatus;
    }

    public void setDeviceStatus(Boolean deviceStatus) {
        isDeviceStatus = deviceStatus;
    }

    public Boolean getShadowDisplay() {
        return isShadowDisplay;
    }

    public void setShadowDisplay(Boolean shadowDisplay) {
        isShadowDisplay = shadowDisplay;
    }

    public Boolean getUserPosition() {
        return isUserPosition;
    }

    public void setUserPosition(Boolean userPosition) {
        isUserPosition = userPosition;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
