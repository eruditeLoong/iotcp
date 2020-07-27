package com.jeecg.position.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**   
 * @Title: Entity
 * @Description: 人员单位
 * @author onlineGenerator
 * @date 2019-10-08 21:23:21
 * @version V1.0   
 *
 */
@Entity
@Table(name = "position_user_org", schema = "")
@SuppressWarnings("serial")
public class PositionUserOrgEntity implements java.io.Serializable {
	/**主键*/
	@Excel(name="单位ID",width=15)
	private java.lang.String id;
	/**创建日期*/
	@Excel(name="创建日期",width=15)
	private java.util.Date createDate;
	/**单位名称*/
	@Excel(name="单位名称",width=15)
	private java.lang.String name;
	/**工作内容*/
	@Excel(name="工作内容",width=15)
	private java.lang.String work;

	/**颜色*/
	@Excel(name="颜色",width=15)
	private java.lang.String color;

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

	@Column(name ="CREATE_DATE",nullable=true,length=20)
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
	 *@return: java.lang.String  单位名称
	 */

	@Column(name ="NAME",nullable=true,length=32)
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工作内容
	 */

	@Column(name ="WORK",nullable=true,length=32)
	public java.lang.String getWork(){
		return this.work;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工作内容
	 */
	public void setWork(java.lang.String work){
		this.work = work;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  颜色
	 */

	@Column(name ="COLOR",nullable=false,length=8)
	public java.lang.String getColor(){
		return this.color;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  颜色
	 */
	public void setColor(java.lang.String color){
		this.color = color;
	}

	@Override
	public String toString() {
		return "PositionUserOrgEntity{" +
				"id='" + id + '\'' +
				", createDate=" + createDate +
				", name='" + name + '\'' +
				", work='" + work + '\'' +
				", color='" + color + '\'' +
				'}';
	}
}