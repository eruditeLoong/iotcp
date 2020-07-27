package com.jeecg.data.entity;

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
 * @Description: 报警数据
 * @author onlineGenerator
 * @date 2019-08-20 20:19:10
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_iot_alarm_data", schema = "")
@SuppressWarnings("serial")
public class IotAlarmDataEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**流程定义ID*/
	private java.lang.String procInsId;
	/**场景*/
	@Excel(name="场景",width=15,dictTable ="jform_scene",dicCode ="id",dicText ="name")
	private java.lang.String sceneBy;
	/**部署设备*/
	@Excel(name="部署设备",width=15,dictTable ="jform_scene_device_depoly",dicCode ="id",dicText ="id")
	private java.lang.String deployDeviceBy;
	/**基础设备*/
	@Excel(name="基础设备",width=15,dictTable ="jform_base_device",dicCode ="id",dicText ="name")
	private java.lang.String baseDeviceBy;
	/**基础设备名称*/
	@Excel(name="基础设备名称",width=15)
	private java.lang.String baseDeviceName;
	/**部署设备编号*/
	@Excel(name="部署设备编号",width=15)
	private java.lang.String deployDeviceCode;
	/**数据名称*/
	@Excel(name="数据名称",width=15)
	private java.lang.String dataLabel;
	/**数据字段*/
	@Excel(name="数据字段",width=15)
	private java.lang.String dataField;
	/**数据值*/
	@Excel(name="数据值",width=15)
	private java.lang.String dataValue;
	/**正常数据范围*/
	@Excel(name="正常数据范围",width=15)
	private java.lang.String normalDataRange;
	/**报警级别*/
	@Excel(name="报警级别",width=15,dicCode="alarmLevel")
	private java.lang.Integer alarmLevel;
	/**处理状态*/
	@Excel(name="处理状态",width=15,dicCode="dealStatus")
	private java.lang.Integer dealStatus;
	/**处理措施*/
	@Excel(name="处理措施",width=15)
	private java.lang.String dealMeasure;
	/**原因分析*/
	@Excel(name="原因分析",width=15)
	private java.lang.String causeAnalysis;
	/**处理人*/
	@Excel(name="处理人",width=15)
	private java.lang.String dealUserBy;
	/**报警时间*/
	@Excel(name="报警时间",width=15,format = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date alarmDate;
	/**处理时间*/
	private java.util.Date dealDate;
	/**流程状态*/
	private java.lang.String bpmStatus;
	
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
	 *@return: java.lang.String  流程定义ID
	 */

	@Column(name ="PROC_INS_ID",nullable=true,length=36)
	public java.lang.String getProcInsId(){
		return this.procInsId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程定义ID
	 */
	public void setProcInsId(java.lang.String procInsId){
		this.procInsId = procInsId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  场景
	 */

	@Column(name ="SCENE_BY",nullable=false,length=36)
	public java.lang.String getSceneBy(){
		return this.sceneBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  场景
	 */
	public void setSceneBy(java.lang.String sceneBy){
		this.sceneBy = sceneBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部署设备
	 */

	@Column(name ="DEPLOY_DEVICE_BY",nullable=false,length=36)
	public java.lang.String getDeployDeviceBy(){
		return this.deployDeviceBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部署设备
	 */
	public void setDeployDeviceBy(java.lang.String deployDeviceBy){
		this.deployDeviceBy = deployDeviceBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  基础设备
	 */

	@Column(name ="BASE_DEVICE_BY",nullable=false,length=36)
	public java.lang.String getBaseDeviceBy(){
		return this.baseDeviceBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  基础设备
	 */
	public void setBaseDeviceBy(java.lang.String baseDeviceBy){
		this.baseDeviceBy = baseDeviceBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  基础设备名称
	 */

	@Column(name ="BASE_DEVICE_NAME",nullable=false,length=32)
	public java.lang.String getBaseDeviceName(){
		return this.baseDeviceName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  基础设备名称
	 */
	public void setBaseDeviceName(java.lang.String baseDeviceName){
		this.baseDeviceName = baseDeviceName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  部署设备编号
	 */

	@Column(name ="DEPLOY_DEVICE_CODE",nullable=false,length=10)
	public java.lang.String getDeployDeviceCode(){
		return this.deployDeviceCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  部署设备编号
	 */
	public void setDeployDeviceCode(java.lang.String deployDeviceCode){
		this.deployDeviceCode = deployDeviceCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数据名称
	 */

	@Column(name ="DATA_LABEL",nullable=false,length=32)
	public java.lang.String getDataLabel(){
		return this.dataLabel;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数据名称
	 */
	public void setDataLabel(java.lang.String dataLabel){
		this.dataLabel = dataLabel;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数据字段
	 */

	@Column(name ="DATA_FIELD",nullable=false,length=32)
	public java.lang.String getDataField(){
		return this.dataField;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数据字段
	 */
	public void setDataField(java.lang.String dataField){
		this.dataField = dataField;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数据值
	 */

	@Column(name ="DATA_VALUE",nullable=false,length=32)
	public java.lang.String getDataValue(){
		return this.dataValue;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数据值
	 */
	public void setDataValue(java.lang.String dataValue){
		this.dataValue = dataValue;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  正常数据范围
	 */

	@Column(name ="NORMAL_DATA_RANGE",nullable=false,length=32)
	public java.lang.String getNormalDataRange(){
		return this.normalDataRange;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  正常数据范围
	 */
	public void setNormalDataRange(java.lang.String normalDataRange){
		this.normalDataRange = normalDataRange;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  报警级别
	 */

	@Column(name ="ALARM_LEVEL",nullable=true,length=1)
	public java.lang.Integer getAlarmLevel(){
		return this.alarmLevel;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  报警级别
	 */
	public void setAlarmLevel(java.lang.Integer alarmLevel){
		this.alarmLevel = alarmLevel;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  处理状态
	 */

	@Column(name ="DEAL_STATUS",nullable=true,length=1)
	public java.lang.Integer getDealStatus(){
		return this.dealStatus;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  处理状态
	 */
	public void setDealStatus(java.lang.Integer dealStatus){
		this.dealStatus = dealStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  处理措施
	 */

	@Column(name ="DEAL_MEASURE",nullable=true,length=128)
	public java.lang.String getDealMeasure(){
		return this.dealMeasure;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  处理措施
	 */
	public void setDealMeasure(java.lang.String dealMeasure){
		this.dealMeasure = dealMeasure;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原因分析
	 */

	@Column(name ="CAUSE_ANALYSIS",nullable=true,length=128)
	public java.lang.String getCauseAnalysis(){
		return this.causeAnalysis;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原因分析
	 */
	public void setCauseAnalysis(java.lang.String causeAnalysis){
		this.causeAnalysis = causeAnalysis;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  处理人
	 */

	@Column(name ="DEAL_USER_BY",nullable=true,length=32)
	public java.lang.String getDealUserBy(){
		return this.dealUserBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  处理人
	 */
	public void setDealUserBy(java.lang.String dealUserBy){
		this.dealUserBy = dealUserBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  报警时间
	 */

	@Column(name ="ALARM_DATE",nullable=true,length=20)
	public java.util.Date getAlarmDate(){
		return this.alarmDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  报警时间
	 */
	public void setAlarmDate(java.util.Date alarmDate){
		this.alarmDate = alarmDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  处理时间
	 */

	@Column(name ="DEAL_DATE",nullable=true,length=20)
	public java.util.Date getDealDate(){
		return this.dealDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  处理时间
	 */
	public void setDealDate(java.util.Date dealDate){
		this.dealDate = dealDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */

	@Column(name ="BPM_STATUS",nullable=true,length=32)
	public java.lang.String getBpmStatus(){
		return this.bpmStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setBpmStatus(java.lang.String bpmStatus){
		this.bpmStatus = bpmStatus;
	}
}