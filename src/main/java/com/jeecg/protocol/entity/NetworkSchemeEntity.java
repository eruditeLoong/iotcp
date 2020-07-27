package com.jeecg.protocol.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 组网方案
 * @author onlineGenerator
 * @date 2018-08-31 16:13:38
 * @version V1.0
 *
 */
@Entity
@Table(name = "jform_network_scheme", schema = "")
@SuppressWarnings("serial")
public class NetworkSchemeEntity implements java.io.Serializable {
	/** 主键 */
	private java.lang.String id;
	/** 方案名称 */
	@Excel(name = "方案名称", width = 15)
	private java.lang.String name;
	/** 接入模式 */
	@Excel(name = "接入模式", width = 15, dictTable = "jform_connection_mode", dicCode = "code", dicText = "name")
	private java.lang.String connectionModeBy;
	/** 通讯方式 */
	@Excel(name = "通讯方式", width = 15, dictTable = "jform_net_mode", dicCode = "code", dicText = "name")
	private java.lang.String netModeBy;
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
	 * @return: java.lang.String 方案名称
	 */

	@Column(name = "NAME", nullable = true, length = 32)
	public java.lang.String getName() {
		return this.name;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 方案名称
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 接入模式
	 */

	@Column(name = "CONNECTION_MODE_BY", nullable = true, length = 36)
	public java.lang.String getConnectionModeBy() {
		return this.connectionModeBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 接入模式
	 */
	public void setConnectionModeBy(java.lang.String connectionModeBy) {
		this.connectionModeBy = connectionModeBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 通讯方式
	 */

	@Column(name = "NET_MODE_BY", nullable = true, length = 36)
	public java.lang.String getNetModeBy() {
		return this.netModeBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 通讯方式
	 */
	public void setNetModeBy(java.lang.String netModeBy) {
		this.netModeBy = netModeBy;
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