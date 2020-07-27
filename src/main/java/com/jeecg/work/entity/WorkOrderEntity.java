package com.jeecg.work.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**   
 * @Title: Entity
 * @Description: 工作票管理
 * @author onlineGenerator
 * @date 2018-10-24 15:41:52
 * @version V1.0   
 *
 */
@Entity
@Table(name = "jform_work_order", schema = "")
@SuppressWarnings("serial")
public class WorkOrderEntity implements java.io.Serializable {
	/**主键*/
	private String id;
	/**标题*/
	@Excel(name="标题",width=15)
	private String title;
	/**工作票扫描件*/
	@Excel(name="工作票扫描件",width=15)
	private String orderFiles;
	/**厂家名称*/
	@Excel(name="厂家名称",width=15)
	private String sysOrgCode;
	@Excel(name="厂家员工",width=512)
	private String personIds;
	/**创建日期*/
	private Date createDate;
	/**备注*/
	@Excel(name="备注",width=15)
	private String remark;
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

	@Column(name ="ID",nullable=false,length=36)
	public String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标题
	 */

	@Column(name ="TITLE",nullable=false,length=50)
	public String getTitle(){
		return this.title;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标题
	 */
	public void setTitle(String title){
		this.title = title;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  工作票扫描件
	 */

	@Column(name ="ORDER_FILES",nullable=false,length=512)
	public String getOrderFiles(){
		return this.orderFiles;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  工作票扫描件
	 */
	public void setOrderFiles(String orderFiles){
		this.orderFiles = orderFiles;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  厂家名称
	 */

	@Column(name ="SYS_ORG_CODE",nullable=false,length=512)
	public String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  厂家名称
	 */
	public void setSysOrgCode(String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}
	
	@Column(name ="	PERSON_IDS",nullable=false,length=512)
	public String getPersonIds() {
		return personIds;
	}

	public void setPersonIds(String personIds) {
		this.personIds = personIds;
	}

	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */

	@Column(name ="CREATE_DATE",nullable=false,length=20)
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */

	@Column(name ="REMARK",nullable=true,length=128)
	public String getRemark(){
		return this.remark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRemark(String remark){
		this.remark = remark;
	}
}