package com.jeecg.protocol.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Title: Entity
 * @Description: 数据协议
 * @author onlineGenerator
 * @date 2018-12-21 19:14:40
 * @version V1.0
 *
 */
@Entity
@Table(name = "jform_data_custom", schema = "")
@SuppressWarnings("serial")
public class DataCustomEntity implements java.io.Serializable {
	/** 主键 */
	private java.lang.String id;
	/** 数据协议 */
	private java.lang.String protocolBy;
	/** 节点名称 */
	@Excel(name = "节点名称", width = 15)
	private java.lang.String nodeName;
	/** 节点标识 */
	@Excel(name = "节点标识", width = 15)
	private java.lang.String nodeField;
	/** 节点索引 */
	@Excel(name = "节点索引", width = 15)
	private java.lang.String nodeIndex;
	/** 正则表达式 */
	@Excel(name = "正则表达式", width = 15)
	private java.lang.String regEx;
	@Excel(name = "数据基本类型", width = 15)
	private java.lang.String datatype;
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
	 * @return: java.lang.String 数据协议
	 */

	@Column(name = "PROTOCOL_BY", nullable = true, length = 36)
	public java.lang.String getProtocolBy() {
		return this.protocolBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 数据协议
	 */
	public void setProtocolBy(java.lang.String protocolBy) {
		this.protocolBy = protocolBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 节点名称
	 */

	@Column(name = "NODE_NAME", nullable = true, length = 32)
	public java.lang.String getNodeName() {
		return this.nodeName;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 节点名称
	 */
	public void setNodeName(java.lang.String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 节点标识
	 */

	@Column(name = "NODE_FIELD", nullable = true, length = 32)
	public java.lang.String getNodeField() {
		return this.nodeField;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 节点标识
	 */
	public void setNodeField(java.lang.String nodeField) {
		this.nodeField = nodeField;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 节点索引
	 */

	@Column(name = "NODE_INDEX", nullable = true, length = 10)
	public java.lang.String getNodeIndex() {
		return this.nodeIndex;
	}

	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String 节点索引
	 */
	public void setNodeIndex(java.lang.String nodeIndex) {
		this.nodeIndex = nodeIndex;
	}

	@Column(name = "REG_EX", nullable = true, length = 100)
	public java.lang.String getRegEx() {
		return regEx;
	}

	public void setRegEx(java.lang.String regEx) {
		this.regEx = regEx;
	}

	@Column(name = "DATA_TYPE", nullable = false, length = 10)
	public java.lang.String getDatatype() {
		return datatype;
	}

	public void setDatatype(java.lang.String datatype) {
		this.datatype = datatype;
	}

	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 备注
	 */

	@Column(name = "REMARK", nullable = true, length = 120)
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
