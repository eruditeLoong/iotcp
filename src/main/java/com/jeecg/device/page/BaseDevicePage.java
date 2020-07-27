package com.jeecg.device.page;

import com.jeecg.device.entity.BaseDeviceDataEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Entity
 * @Description: 基础设备信息
 * @author onlineGenerator
 * @date 2018-08-24 18:08:53
 * @version V1.0
 *
 */
public class BaseDevicePage implements java.io.Serializable {
	@Autowired
	private SystemService systemService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private java.lang.String id;
	/** 设备名称 */
	@Excel(name = "设备名称")
	private java.lang.String name;
	/** 设备类型 */
	@Excel(name = "设备类型")
	private java.lang.String type;
	/** 通讯方式 */
	@Excel(name = "通讯方式")
	private java.lang.String comMethod;
	/** 通讯协议 */
	@Excel(name = "通讯协议", width = 15, dicCode = "jform_net_mode")
	private java.lang.String comProtocol;
	/** 数据传输协议 */
	@Excel(name = "数据格式", width = 15, dicCode = "jform_net_mode")
	private java.lang.String dataFormat;
	/** 设备工作模式 */
	@Excel(name = "设备工作模式", width = 15, dicCode = "bd_workModel")
	private java.lang.String workModel;
	/** 设备工作模式 */
	@Excel(name = "设备工作模式", width = 6)
	private java.lang.Integer requestTime;
	/** 模型文件 */
	@Excel(name = "模型文件")
	private java.lang.String modelFile;
	/** 状态 */
	@Excel(name = "状态", width = 15, dicCode = "isActive")
	private java.lang.Integer status;
	/** 创建人 */
	@Excel(name = "创建人")
	private java.lang.String createBy;
	/** 创建日期 */
	@Excel(name = "创建日期", format = "yyyy-MM-dd")
	private java.util.Date createDate;
	/** 更新人 */
	@Excel(name = "更新人")
	private java.lang.String updateBy;
	/** 更新日期 */
	@Excel(name = "更新日期", format = "yyyy-MM-dd")
	private java.util.Date updateDate;
	/** 描述 */
	@Excel(name = "描述")
	private java.lang.String description;

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 主键
	 */
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
	public java.lang.String getComProtocol() {
		return comProtocol;
	}

	public void setComProtocol(java.lang.String comProtocol) {
		this.comProtocol = comProtocol;
	}

	/**
	 * 数据格式
	 * 
	 * @return
	 */
	public java.lang.String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(java.lang.String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public java.lang.String getWorkModel() {
		return workModel;
	}

	public void setWorkModel(java.lang.String workModel) {
		this.workModel = workModel;
	}

	public java.lang.Integer getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(java.lang.Integer requestTime) {
		this.requestTime = requestTime;
	}

	public java.lang.String getModelFile() {
		return modelFile;
	}

	public void setModelFile(java.lang.String modelFile) {
		this.modelFile = modelFile;
	}

	public java.lang.Integer getStatus() {
		return status;
	}

	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 创建人
	 */
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

	/** 保存-基础设备数据点 */
	@ExcelCollection(name = "基础设备数据点")
	private List<BaseDeviceDataEntity> baseDeviceDataList = new ArrayList<BaseDeviceDataEntity>();

	public List<BaseDeviceDataEntity> getBaseDeviceDataList() {
		return baseDeviceDataList;
	}

	public void setBaseDeviceDataList(List<BaseDeviceDataEntity> baseDeviceDataList) {
		this.baseDeviceDataList = baseDeviceDataList;
	}

}
