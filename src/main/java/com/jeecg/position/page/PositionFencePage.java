package com.jeecg.position.page;

import com.jeecg.position.entity.PositionFencePointEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Title: Entity
 * @Description: 围栏信息
 * @author onlineGenerator
 * @date 2019-10-06 22:25:53
 * @version V1.0   
 *
 */
public class PositionFencePage implements java.io.Serializable {
	/**主键*/
	private java.lang.String id;
	/**所属场景*/
    @Excel(name="所属场景")
	private java.lang.String sceneBy;
	/**名称*/
    @Excel(name="名称")
	private java.lang.String name;
	/**围栏*/
    @Excel(name="围栏")
	private java.lang.Integer type;
	/**颜色*/
    @Excel(name="颜色")
	private java.lang.String color;
	/**
	 * 是否围栏边界
	 */
	@Excel(name = "是否围栏边界")
	private java.lang.Integer isSceneBoundary;
	/**创建日期*/
	private java.util.Date createDate;
	/**备注*/
    @Excel(name="备注")
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
	 *@return: java.lang.String  所属场景
	 */
	public java.lang.String getSceneBy(){
		return this.sceneBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属场景
	 */
	public void setSceneBy(java.lang.String sceneBy){
		this.sceneBy = sceneBy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  名称
	 */
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  名称
	 */
	public void setName(java.lang.String name){
		this.name = name;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  围栏
	 */
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  围栏
	 */
	public void setType(java.lang.Integer type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  颜色
	 */
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
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer  是否围栏边界
	 */
	public Integer getIsSceneBoundary() {
		return this.isSceneBoundary;
	}

	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer  是否围栏边界
	 */
	public void setIsSceneBoundary(Integer isSceneBoundary) {
		this.isSceneBoundary = isSceneBoundary;
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
	 *@return: java.lang.String  备注
	 */
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
	
	/**保存-围栏顶点集合*/
    @ExcelCollection(name="围栏顶点集合")
	private List<PositionFencePointEntity> potionFencePointList = new ArrayList<PositionFencePointEntity>();
	public List<PositionFencePointEntity> getPotionFencePointList() {
		return potionFencePointList;
	}
	public void setPotionFencePointList(List<PositionFencePointEntity> potionFencePointList) {
		this.potionFencePointList = potionFencePointList;
	}
	
}
