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
 * @Description: 网络通讯方式
 * @author onlineGenerator
 * @date 2018-09-01 10:20:32
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_net_mode", schema = "")
@SuppressWarnings("serial")
public class NetModeEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**模式名称*/
	@Excel(name="模式名称",width=15)
	private java.lang.String name;
	/**模式编码*/
	@Excel(name="模式编码",width=15)
	private java.lang.String code;
	/**模式编码*/
	@Excel(name="图标",width=15)
	private java.lang.String icon;
	/**顺序*/
	@Excel(name="顺序",width=15)
	private java.lang.Integer num;
	
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
	 *@return: java.lang.String  模式名称
	 */

	@Column(name ="NAME",nullable=true,length=32)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模式名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  模式编码
	 */

	@Column(name ="CODE",nullable=true,length=32)
	public java.lang.String getCode(){
		return this.code;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  模式编码
	 */
	public void setCode(java.lang.String code){
		this.code = code;
	}
	
	@Column(name ="icon",nullable=false,length=200)
	public java.lang.String getIcon() {
		return icon;
	}

	public void setIcon(java.lang.String icon) {
		this.icon = icon;
	}

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  顺序
	 */

	@Column(name ="NUM",nullable=true,length=2)
	public java.lang.Integer getNum(){
		return this.num;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  顺序
	 */
	public void setNum(java.lang.Integer num){
		this.num = num;
	}
}