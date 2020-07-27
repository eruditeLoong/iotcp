package com.jeecg.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

/**   
 * @Title: Entity
 * @Description: 物联网数据
 * @author onlineGenerator
 * @date 2018-08-24 17:26:23
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_iot_data", schema = "")
@SuppressWarnings("serial")
public class IotDataEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**创建日期*/
	@Excel(name="创建日期",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createDate;
	/**设备*/
	@Excel(name="设备",width=15,dictTable ="jform_instance_device",dicCode ="code",dicText ="name")
	private java.lang.String instanceDeviceBy;
	/**测量项目名称*/
	@Excel(name="测量项目名称",width=15)
	private java.lang.String label;
	/**测量项目*/
	@Excel(name="测量项目",width=15)
	private java.lang.String fieldBy;
	/**数值*/
	@Excel(name="数值",width=15)
	private java.lang.String data;
	/**数值类型*/
	@Excel(name="数值类型",width=15,dicCode="iot_date_t")
	private java.lang.String type;
	/**状态*/
	@Excel(name="状态",width=15)
	private java.lang.Integer status;
	/**方向*/
	@Excel(name="方向",width=15)
	private java.lang.Integer direction;
	/**备注*/
	@Excel(name="备注",width=15)
	private java.lang.String remark;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

	@Column(name ="ID",nullable=false,length=36)
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
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */

	@Column(name ="CREATE_DATE",nullable=false,length=20)
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
	 *@return: java.lang.String  设备
	 */

	@Column(name ="INSTANCE_DEVICE_BY",nullable=false,length=36)
	public java.lang.String getInstanceDeviceBy(){
		return this.instanceDeviceBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  设备
	 */
	public void setInstanceDeviceBy(java.lang.String instanceDeviceBy){
		this.instanceDeviceBy = instanceDeviceBy;
	}
	public java.lang.String getLabel() {
		return label;
	}

	public void setLabel(java.lang.String label) {
		this.label = label;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  测量项目
	 */

	@Column(name ="FIELD_BY",nullable=false,length=32)
	public java.lang.String getFieldBy(){
		return this.fieldBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  测量项目
	 */
	public void setFieldBy(java.lang.String fieldBy){
		this.fieldBy = fieldBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数值
	 */

	@Column(name ="DATA",nullable=false,length=32)
	public java.lang.String getData(){
		return this.data;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数值
	 */
	public void setData(java.lang.String data){
		this.data = data;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数值类型
	 */

	@Column(name ="TYPE",nullable=false,length=1)
	public java.lang.String getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数值类型
	 */
	public void setType(java.lang.String type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */

	@Column(name ="STATUS",nullable=false,length=1)
	public java.lang.Integer getStatus(){
		return this.status;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	
	@Column(name ="DIRECTION",nullable=false,length=1)
	public java.lang.Integer getDirection() {
		return direction;
	}

	public void setDirection(java.lang.Integer direction) {
		this.direction = direction;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */

	@Column(name ="REMARK",nullable=true,length=80)
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRemark(java.lang.String remark){
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "IotDataEntity [id=" + id + ", createDate=" + createDate + ", instanceDeviceBy=" + instanceDeviceBy
				+ ", label=" + label + ", fieldBy=" + fieldBy + ", data=" + data + ", type=" + type + ", status="
				+ status + ", direction=" + direction + ", remark=" + remark + "]";
	}
	
	
}