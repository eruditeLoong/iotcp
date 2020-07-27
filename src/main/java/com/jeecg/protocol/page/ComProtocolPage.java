package com.jeecg.protocol.page;

import com.jeecg.protocol.entity.ComProtocolEntity;
import com.jeecg.protocol.entity.ComConfigEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.SequenceGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;

/**   
 * @Title: Entity
 * @Description: 通讯协议
 * @author onlineGenerator
 * @date 2019-05-21 19:07:32
 * @version V1.0   
 *
 */
public class ComProtocolPage implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**协议名称*/
    @Excel(name="协议名称")
	private java.lang.String name;
	/**协议code*/
    @Excel(name="协议code")
	private java.lang.String code;
	/**校验类型*/
    @Excel(name="校验类型")
	private java.lang.String checkType;
	/**数据模式*/
    @Excel(name="数据模式")
	private java.lang.String dataMode;
	/**创建人*/
    @Excel(name="创建人")
	private java.lang.String createBy;
	/**创建日期*/
    @Excel(name="创建日期",format = "yyyy-MM-dd")
	private java.util.Date createDate;
	/**更新人*/
    @Excel(name="更新人")
	private java.lang.String updateBy;
	/**更新日期*/
    @Excel(name="更新日期",format = "yyyy-MM-dd")
	private java.util.Date updateDate;
	/**所属部门*/
    @Excel(name="所属部门")
	private java.lang.String sysOrgCode;
	/**所属公司*/
    @Excel(name="所属公司")
	private java.lang.String sysCompanyCode;
	/**协议描述*/
    @Excel(name="协议描述")
	private java.lang.String remark;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	public java.lang.String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(java.lang.String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  协议名称
	 */
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  协议名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  协议code
	 */
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  协议code
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  校验类型
	 */
	public java.lang.String getCheckType(){
		return this.checkType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  校验类型
	 */
	public void setCheckType(java.lang.String checkType){
		this.checkType = checkType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数据模式
	 */
	public java.lang.String getDataMode(){
		return this.dataMode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数据模式
	 */
	public void setDataMode(java.lang.String dataMode){
		this.dataMode = dataMode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人
	 */
	public java.lang.String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	public java.util.Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人
	 */
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属部门
	 */
	public java.lang.String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属部门
	 */
	public void setSysOrgCode(java.lang.String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属公司
	 */
	public java.lang.String getSysCompanyCode(){
		return this.sysCompanyCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属公司
	 */
	public void setSysCompanyCode(java.lang.String sysCompanyCode){
		this.sysCompanyCode = sysCompanyCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  协议描述
	 */
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  协议描述
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}
	
	/**保存-协议配置*/
    @ExcelCollection(name="协议配置")
	private List<ComConfigEntity> comConfigList = new ArrayList<ComConfigEntity>();
	public List<ComConfigEntity> getComConfigList() {
		return comConfigList;
	}
	public void setComConfigList(List<ComConfigEntity> comConfigList) {
		this.comConfigList = comConfigList;
	}
	
}
