package com.jeecg.scene.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 场景设备
 * @date 2019-04-17 22:32:11
 */
@Entity
@Table(name = "jform_scene_device", schema = "")
public class SceneDeviceEntity implements java.io.Serializable {
    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * 场景
     */
    @Excel(name = "场景", width = 15)
    private java.lang.String sceneBy;
    /**
     * 设备
     */
    @Excel(name = "设备", width = 15)
    private java.lang.String deviceBy;

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
     * @return: java.lang.String 场景
     */

    @Column(name = "SCENE_BY", nullable = false, length = 36)
    public java.lang.String getSceneBy() {
        return this.sceneBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 场景
     */
    public void setSceneBy(java.lang.String sceneBy) {
        this.sceneBy = sceneBy;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 设备
     */

    @Column(name = "DEVICE_BY", nullable = true, length = 36)
    public java.lang.String getDeviceBy() {
        return this.deviceBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 设备
     */
    public void setDeviceBy(java.lang.String deviceBy) {
        this.deviceBy = deviceBy;
    }
}