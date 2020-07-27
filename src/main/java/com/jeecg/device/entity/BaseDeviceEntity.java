package com.jeecg.device.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 基础设备信息
 * @date 2018-08-24 18:08:53
 */
@Entity
@Table(name = "jform_base_device", schema = "")
public class BaseDeviceEntity implements java.io.Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private java.lang.String id;
    /**
     * 设备名称
     */
    @Excel(name = "设备名称", width = 15)
    private java.lang.String name;
    /**
     * 设备类型
     */
    @Excel(name = "设备类型", width = 15, dicCode = "bd_type")
    private java.lang.String type;
    /**
     * 通讯方式
     */
    @Excel(name = "通讯方式", width = 15, dicCode = "sf_yn")
    private java.lang.String comMethod;
    /**
     * 通讯协议
     */
    @Excel(name = "通讯协议", width = 15, dicCode = "jform_net_mode")
    private java.lang.String comProtocol;
    /**
     * 数据传输协议
     */
    @Excel(name = "数据格式", width = 15, dicCode = "jform_net_mode")
    private java.lang.String dataFormat;
	/**
	 * 请求命令数据类型
	 */
	@Excel(name = "请求命令数据类型", width = 15, dicCode = "jform_net_mode")
    private java.lang.String cmdDataType;
    /**
     * 设备工作模式
     */
    @Excel(name = "设备工作模式", width = 15, dicCode = "bd_workModel")
    private java.lang.String workModel;
    /**
     * 轮询时间
     */
    @Excel(name = "轮询时间", width = 15) // 1000 * 60
    private java.lang.Integer requestTime;
    /**
     * 模型文件
     */
    @Excel(name = "模型文件", width = 30)
    private java.lang.String modelFile;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15, dicCode = "isActive")
    private java.lang.Integer status;
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
     * 描述
     */
    @Excel(name = "描述", width = 15)
    private java.lang.String description;

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
     * @return: java.lang.String 设备名称
     */

    @Column(name = "NAME", nullable = false, length = 32)
    public java.lang.String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 设备名称
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 设备类型
     */

    @Column(name = "TYPE", nullable = false, length = 32)
    public java.lang.String getType() {
        return this.type;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 设备类型
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 通讯方式
     */

    @Column(name = "COM_METHOD", nullable = true, length = 36)
    public java.lang.String getComMethod() {
        return this.comMethod;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 通讯方式
     */
    public void setComMethod(java.lang.String comMethod) {
        this.comMethod = comMethod;
    }

    /**
     * 通讯协议
     *
     * @return
     */
    @Column(name = "COM_PROTOCOL", nullable = false, length = 36)
    public java.lang.String getComProtocol() {
        return comProtocol;
    }

    public void setComProtocol(java.lang.String comProtocol) {
        this.comProtocol = comProtocol;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 数据数据格式
     */

    @Column(name = "DATA_FORMAT", nullable = false, length = 36)
    public java.lang.String getDataFormat() {
        return this.dataFormat;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 数据格式
     */
    public void setDataFormat(java.lang.String dataFormat) {
        this.dataFormat = dataFormat;
    }

    @Column(name = "WORK_MODEL", nullable = false, length = 32)
    public java.lang.String getWorkModel() {
        return workModel;
    }

    public void setWorkModel(java.lang.String workModel) {
        this.workModel = workModel;
    }

    @Column(name = "POLL_TIME", nullable = false, length = 10)
    public java.lang.Integer getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(java.lang.Integer requestTime) {
        this.requestTime = requestTime;
    }

    @Column(name = "MODEL_FILE", nullable = false, length = 200)
    public java.lang.String getModelFile() {
        return modelFile;
    }

    public void setModelFile(java.lang.String modelFile) {
        this.modelFile = modelFile;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS", nullable = false, length = 1)
    public java.lang.Integer getStatus() {
        return this.status;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer 状态
     */
    public void setStatus(java.lang.Integer status) {
        this.status = status;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 创建人
     */

    @Column(name = "CREATE_BY", nullable = false, length = 50)
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

    @Column(name = "UPDATE_BY", nullable = true, length = 50)
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
     * @return: java.lang.String 描述
     */

    @Column(name = "DESCRIPTION", nullable = true, length = 128)
    public java.lang.String getDescription() {
        return this.description;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 描述
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

}
