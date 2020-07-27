package com.jeecg.position.entity;
import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**   
 * @Title: Entity
 * @Description: 围栏信息
 * @author onlineGenerator
 * @date 2019-10-06 22:25:53
 * @version V1.0   
 *
 */
@Entity
@Table(name = "position_fence_point", schema = "")
@SuppressWarnings("serial")
public class PositionFencePointEntity implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**所属围栏*/
	private java.lang.String fenceBy;
	/**顶点*/
	@Excel(name="顶点",width=15,dicCode="bpm_status")
	private java.lang.String point;
	/**排序*/
	@Excel(name="排序",width=15)
	private java.lang.Integer sort;
	
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
	 *@return: java.lang.String  所属围栏
	 */
	
	@Column(name ="FENCE_BY",nullable=false,length=36)
	public java.lang.String getFenceBy(){
		return this.fenceBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属围栏
	 */
	public void setFenceBy(java.lang.String fenceBy){
		this.fenceBy = fenceBy;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  顶点
	 */
	
	@Column(name ="POINT",nullable=false,length=100)
	public java.lang.String getPoint(){
		return this.point;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  顶点
	 */
	public void setPoint(java.lang.String point){
		this.point = point;
	}
	
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  排序
	 */
	
	@Column(name ="SORT",nullable=true,length=32)
	public java.lang.Integer getSort(){
		return this.sort;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  排序
	 */
	public void setSort(java.lang.Integer sort){
		this.sort = sort;
	}
	
}
