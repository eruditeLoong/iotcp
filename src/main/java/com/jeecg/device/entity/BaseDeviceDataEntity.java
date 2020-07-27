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
 * @Description: 基础设备信息
 * @author onlineGenerator
 * @date 2018-08-24 18:08:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_base_device_data", schema = "")
@SuppressWarnings("serial")
public class BaseDeviceDataEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**设备*/
	private java.lang.String deviceBy;
	/**显示名称*/
	@Excel(name="显示名称",width=15)
	private java.lang.String name;
	/**数据标识*/
	@Excel(name="数据标识",width=15)
	private java.lang.String field;
	/**数据单位*/
	@Excel(name="数据单位",width=15)
	private java.lang.String unit;
	/**读写类型*/
	@Excel(name="读写类型",width=15,dicCode="sf_yn")
	private java.lang.String rwType;
	/**数据类型*/
	@Excel(name="数据类型",width=15,dicCode="sf_yn")
	private java.lang.String dataType;
	/**数据下线值*/
	@Excel(name="数据范围",width=15)
	private java.lang.String dataRange;
	/**数据上线值*/
	@Excel(name="正常数据范围",width=15)
	private java.lang.String normalDataRange;
	/**精度*/
	@Excel(name="精度",width=15)
	private java.lang.Integer accuracy;
	/**枚举范围*/
	@Excel(name="枚举范围",width=15)
	private java.lang.String enuRange;
	/**备注*/
	@Excel(name="备注",width=15)
	private java.lang.String remarks;
	
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  设备
	 */
	
	@Column(name ="DEVICE_BY",nullable=false,length=36)
	public java.lang.String getDeviceBy(){
		return this.deviceBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  设备
	 */
	public void setDeviceBy(java.lang.String deviceBy){
		this.deviceBy = deviceBy;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  显示名称
	 */
	
	@Column(name ="NAME",nullable=false,length=32)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  显示名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数据标识
	 */
	
	@Column(name ="FIELD",nullable=false,length=32)
	public java.lang.String getField(){
		return this.field;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数据标识
	 */
	public void setField(java.lang.String field){
		this.field = field;
	}
	
	@Column(name ="UNIT",nullable=false,length=32)
	public java.lang.String getUnit() {
		return unit;
	}

	public void setUnit(java.lang.String unit) {
		this.unit = unit;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  读写类型
	 */
	
	@Column(name ="RW_TYPE",nullable=false,length=2)
	public java.lang.String getRwType(){
		return this.rwType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  读写类型
	 */
	public void setRwType(java.lang.String rwType){
		this.rwType = rwType;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数据类型
	 */
	
	@Column(name ="DATA_TYPE",nullable=false,length=2)
	public java.lang.String getDataType(){
		return this.dataType;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数据类型
	 */
	public void setDataType(java.lang.String dataType){
		this.dataType = dataType;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  数据范围
	 */
	
	@Column(name ="DATA_RANGE",nullable=true,length=50)
	public java.lang.String getDataRange(){
		return this.dataRange;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  数据范围
	 */
	public void setDataRange(java.lang.String dataRange){
		this.dataRange = dataRange;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  正常数据范围
	 */
	
	@Column(name ="NORMAL_DATA_RANGE",nullable=true,length=50)
	public java.lang.String getNormalDataRange(){
		return this.normalDataRange;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  正常数据范围
	 */
	public void setNormalDataRange(java.lang.String normalDataRange){
		this.normalDataRange = normalDataRange;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  精度
	 */
	
	@Column(name ="ACCURACY",nullable=true,length=5)
	public java.lang.Integer getAccuracy(){
		return this.accuracy;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  精度
	 */
	public void setAccuracy(java.lang.Integer accuracy){
		this.accuracy = accuracy;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  枚举范围
	 */
	
	@Column(name ="ENU_RANGE",nullable=true,length=50)
	public java.lang.String getEnuRange(){
		return this.enuRange;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  枚举范围
	 */
	public void setEnuRange(java.lang.String enuRange){
		this.enuRange = enuRange;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	
	@Column(name ="REMARKS",nullable=true,length=80)
	public java.lang.String getRemarks(){
		return this.remarks;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRemarks(java.lang.String remarks){
		this.remarks = remarks;
	}
	
}
