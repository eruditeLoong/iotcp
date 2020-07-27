package com.jeecg.device.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 应用设备
 * @author onlineGenerator
 * @date 2018-12-21 16:14:00
 * @version V1.0
 *
 */
@Entity
@Table(name = "jform_instance_device", schema = "")
@SuppressWarnings("serial")
public class InstanceDeviceEntity implements java.io.Serializable {
	/** 主键 */
	private java.lang.String id;
	/** 名称 */
	@Excel(name = "名称", width = 15)
	private java.lang.String name;
	/** 编码 */
	@Excel(name = "编码", width = 15)
	private java.lang.String code;
	/** 设备层级 */
	@Excel(name = "设备层级", width = 15, dicCode = "devLevel")
	private java.lang.Integer level;
	/** 上级设备 */
	@Excel(name = "上级设备", width = 15, dictTable = "jform_instance_device", dicCode = "id", dicText = "name")
	private java.lang.String parentBy;
	/** 基础设备 */
	@Excel(name = "基础设备", width = 15, dictTable = "jform_base_device", dicCode = "id", dicText = "name")
	private java.lang.String baseDeviceBy;
	/** 位置坐标 */
	@Excel(name = "位置坐标", width = 15)
	private java.lang.String coordinate;
	/** 创建人 */
	private java.lang.String createBy;
	/** 创建日期 */
	private java.util.Date createDate;
	/** 更新人 */
	private java.lang.String updateBy;
	/** 更新日期 */
	private java.util.Date updateDate;
	/** 所属部门 */
	private java.lang.String sysOrgCode;
	/** 所属公司 */
	private java.lang.String sysCompanyCode;
	/** 备注 */
	@Excel(name = "备注", width = 15)
	private java.lang.String remark;

	private BaseDeviceEntity baseDevice;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "base_device_by", insertable = false, updatable = false)
	public BaseDeviceEntity getBaseDevice() {
		return this.baseDevice;
	}

	public void setBaseDevice(BaseDeviceEntity baseDevice) {
		this.baseDevice = baseDevice;
	}

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
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 设备层级
	 */
	@Column(name = "LEVEL", nullable = false, length = 1)
	public java.lang.Integer getLevel() {
		return this.level;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer 设备层级
	 */
	public void setLevel(java.lang.Integer level) {
		this.level = level;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 上级设备
	 */
	@Column(name = "PARENT_BY", nullable = true, length = 36)
	public java.lang.String getParentBy() {
		return this.parentBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 上级设备
	 */
	public void setParentBy(java.lang.String parentBy) {
		this.parentBy = parentBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 基础设备
	 */
	@Column(name = "BASE_DEVICE_BY", nullable = false, length = 36)
	public java.lang.String getBaseDeviceBy() {
		return this.baseDeviceBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 基础设备
	 */
	public void setBaseDeviceBy(java.lang.String baseDeviceBy) {
		this.baseDeviceBy = baseDeviceBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 位置坐标
	 */
	@Column(name = "COORDINATE", nullable = true, length = 200)
	public java.lang.String getCoordinate() {
		return this.coordinate;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 位置坐标
	 */
	public void setCoordinate(java.lang.String coordinate) {
		this.coordinate = coordinate;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 创建人
	 */
	@Column(name = "CREATE_BY", nullable = true, length = 50)
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
	 * @return: java.lang.String 所属部门
	 */
	@Column(name = "SYS_ORG_CODE", nullable = true, length = 50)
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
	@Column(name = "SYS_COMPANY_CODE", nullable = true, length = 50)
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
	@Column(name = "REMARK", nullable = true, length = 32)
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
