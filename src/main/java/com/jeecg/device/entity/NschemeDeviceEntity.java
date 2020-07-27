package com.jeecg.device.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 组网方案绑定设备
 * @author onlineGenerator
 * @date 2018-08-31 18:47:28
 * @version V1.0
 *
 */
@Entity
@Table(name = "jform_nscheme_device", schema = "")
@SuppressWarnings("serial")
public class NschemeDeviceEntity implements java.io.Serializable {
	/** 主键 */
	private java.lang.String id;
	/** 所属方案 */
	@Excel(name = "设备标题", width = 35)
	private java.lang.String title;
	/** 所属方案 */
	@Excel(name = "所属方案", width = 15, dictTable = "jform_network_scheme", dicCode = "id", dicText = "name")
	private java.lang.String schemeBy;
	/** 所属网关 */
	@Excel(name = "所属网关", width = 15, dictTable = "jform_net_mode", dicCode = "code", dicText = "name")
	private java.lang.String gatewayBy;
	/** 设备 */
	@Excel(name = "设备", width = 15, dictTable = "jform_base_device", dicCode = "id", dicText = "name")
	private java.lang.String deviceBy;
	/** 设备编号 */
	@Excel(name = "设备编号", width = 15)
	private java.lang.String deviceCode;
	/** 位置信息 */
	@Excel(name = "位置信息", width = 15)
	private java.lang.String gisInfo;
	/** 状态 */
	@Excel(name = "状态", width = 15, dicCode = "isActive")
	private java.lang.String status;
	/** 创建人 */
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/** 创建日期 */
	@Excel(name = "创建日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createDate;
	/** 更新人 */
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/** 更新日期 */
	@Excel(name = "更新日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateDate;
	/** 所属部门 */
	@Excel(name = "所属部门", width = 15)
	private java.lang.String sysOrgCode;
	/** 所属公司 */
	@Excel(name = "所属公司", width = 15)
	private java.lang.String sysCompanyCode;
	/** 备注 */
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
	 * @return: java.lang.String 所属方案
	 */
	@Column(name = "SCHEME_BY", nullable = true, length = 36)
	public java.lang.String getSchemeBy() {
		return this.schemeBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 所属方案
	 */
	public void setSchemeBy(java.lang.String schemeBy) {
		this.schemeBy = schemeBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 所属网关
	 */
	@Column(name = "GATEWAY_BY", nullable = true, length = 36)
	public java.lang.String getGatewayBy() {
		return this.gatewayBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 所属网关
	 */
	public void setGatewayBy(java.lang.String gatewayBy) {
		this.gatewayBy = gatewayBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 设备
	 */
	@Column(name = "DEVICE_BY", nullable = false, length = 36)
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

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 设备编号
	 */
	@Column(name = "DEVICE_CODE", nullable = true, length = 36)
	public java.lang.String getDeviceCode() {
		return this.deviceCode;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 设备编号
	 */
	public void setDeviceCode(java.lang.String deviceCode) {
		this.deviceCode = deviceCode;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 位置信息
	 */
	@Column(name = "GIS_INFO", nullable = true, length = 36)
	public java.lang.String getGisInfo() {
		return this.gisInfo;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 位置信息
	 */
	public void setGisInfo(java.lang.String gisInfo) {
		this.gisInfo = gisInfo;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 状态
	 */
	@Column(name = "STATUS", nullable = true, length = 2)
	public java.lang.String getStatus() {
		return this.status;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 状态
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 创建人
	 */
	@Column(name = "CREATE_BY", nullable = true, length = 36)
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
	@Column(name = "CREATE_DATE", nullable = true, length = 20)
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
	 * @return: java.lang.String 所属部门
	 */
	@Column(name = "SYS_ORG_CODE", nullable = true, length = 36)
	public java.lang.String getSysOrgCode() {
		return this.sysOrgCode;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 所属部门
	 */
	public void setSysOrgCode(java.lang.String sysOrgCode) {
		this.sysOrgCode = sysOrgCode;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 所属公司
	 */
	@Column(name = "SYS_COMPANY_CODE", nullable = true, length = 36)
	public java.lang.String getSysCompanyCode() {
		return this.sysCompanyCode;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 所属公司
	 */
	public void setSysCompanyCode(java.lang.String sysCompanyCode) {
		this.sysCompanyCode = sysCompanyCode;
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

	@Column(name = "TITLE", nullable = false, length = 50)
	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}
}