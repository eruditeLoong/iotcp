package com.jeecg.position.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 围栏信息
 * @date 2019-10-06 22:25:53
 */
@Entity
@Table(name = "position_fence", schema = "")
@SuppressWarnings("serial")
public class PositionFenceEntity implements java.io.Serializable {
    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * 所属场景
     */
    @Excel(name = "所属场景", width = 15)
    private java.lang.String sceneBy;
    /**
     * 名称
     */
    @Excel(name = "名称", width = 15)
    private java.lang.String name;
    /**
     * 围栏
     */
    @Excel(name = "围栏", width = 15, dicCode = "fenceType")
    private java.lang.Integer type;
    /**
     * 颜色
     */
    @Excel(name = "颜色", width = 15)
    private java.lang.String color;
    /**
     * 是否围栏边界
     */
    @Excel(name = "是否围栏边界", width = 15)
    private java.lang.Integer isSceneBoundary;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    private java.lang.String remark;

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
     * 法: 设置java.lang.String
     *
     * @param: java.lang.String  主键
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  所属场景
     */

    @Column(name = "SCENE_BY", nullable = false, length = 36)
    public java.lang.String getSceneBy() {
        return this.sceneBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  所属场景
     */
    public void setSceneBy(java.lang.String sceneBy) {
        this.sceneBy = sceneBy;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  名称
     */

    @Column(name = "NAME", nullable = false, length = 32)
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  名称
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  围栏
     */

    @Column(name = "TYPE", nullable = false, length = 1)
    public java.lang.Integer getType() {
        return this.type;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  围栏
     */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  颜色
     */

    @Column(name = "COLOR", nullable = false, length = 8)
    public java.lang.String getColor() {
        return this.color;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  颜色
     */
    public void setColor(java.lang.String color) {
        this.color = color;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  是否围栏边界
     */
    public void setIsSceneBoundary(java.lang.Integer isSceneBoundary) {
        this.isSceneBoundary = isSceneBoundary;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  是否围栏边界
     */
    @Column(name = "IS_SCENE_BOUNDARY", nullable = false, length = 1)
    public java.lang.Integer getIsSceneBoundary() {
        return this.isSceneBoundary;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */

    @Column(name = "CREATE_DATE", nullable = true, length = 20)
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

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  备注
     */

    @Column(name = "REMARK", nullable = true, length = 120)
    public java.lang.String getRemark() {
        return this.remark;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  备注
     */
    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

}
