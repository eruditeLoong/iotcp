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
 * @Description: 通讯协议
 * @author onlineGenerator
 * @date 2019-05-21 19:07:32
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_com_config", schema = "")
@SuppressWarnings("serial")
public class ComConfigEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**通讯协议*/
	private java.lang.String comProtocolBy;
	/**参数标题*/
	@Excel(name="参数标题",width=15)
	private java.lang.String title;
	/**参数变量名*/
	@Excel(name="参数变量名",width=15)
	private java.lang.String name;
	/**参数变量值*/
	@Excel(name="参数变量值",width=15)
	private java.lang.String value;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  通讯协议
	 */
	
	@Column(name ="COM_PROTOCOL_BY",nullable=false,length=36)
	public java.lang.String getComProtocolBy(){
		return this.comProtocolBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  通讯协议
	 */
	public void setComProtocolBy(java.lang.String comProtocolBy){
		this.comProtocolBy = comProtocolBy;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数标题
	 */
	
	@Column(name ="TITLE",nullable=false,length=32)
	public java.lang.String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数标题
	 */
	public void setTitle(java.lang.String title){
		this.title = title;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数变量名
	 */
	
	@Column(name ="NAME",nullable=false,length=32)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数变量名
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  参数变量值
	 */
	
	@Column(name ="VALUE",nullable=false,length=32)
	public java.lang.String getValue(){
		return this.value;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  参数变量值
	 */
	public void setValue(java.lang.String value){
		this.value = value;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	
	@Column(name ="REMARK",nullable=true,length=100)
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
	
}
