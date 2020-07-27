package com.jeecg.scene.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Entity
 * @Description: 场景部署设备
 * @date 2019-04-18 22:50:38
 */
@Entity
@Table(name = "jform_scene_device_depoly", schema = "")
@SuppressWarnings("serial")
public class SceneDeviceDepolyEntity implements java.io.Serializable {
	/**
	 * id
	 */
	private java.lang.String id;
	/**
	 * 场景
	 */
	@Excel(name = "场景", width = 15, dictTable = "jform_scene", dicCode = "id", dicText = "name")
	private java.lang.String sceneBy;
	/**
	 * 设备
	 */
	@Excel(name = "设备", width = 15, dictTable = "jform_base_device", dicCode = "id", dicText = "name")
	private java.lang.String deviceBy;
	/**
	 * 设备
	 */
	@Excel(name = "父级设备", width = 15)
	private java.lang.String deviceParentBy;
	/**
	 * 设备编码
	 */
	@Excel(name = "设备编码", width = 15, type = 1)
	private java.lang.String deviceCode;
	/**
	 * 设备地址
	 */
	@Excel(name = "设备地址", width = 15)
	private java.lang.String deviceAddress;

	/**
	 * 是否发送命令
	 */
	@Excel(name = "是否发送命令", width = 15, dicCode = "sf_yn")
	private java.lang.String isSendCmd;
	/**
	 * 请求命令数据类型
	 */
	@Excel(name = "请求命令数据类型", width = 15, dicCode = "dataShape")
	private java.lang.String cmdDataType;
	/**
	 * 请求命令
	 */
	@Excel(name = "请求命令", width = 15)
	private java.lang.String requestCmd;
	/**
	 * 设备位置
	 */
	@Excel(name = "设备位置", width = 15)
	private java.lang.String threeData;

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String id
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

	@Column(name = "ID", nullable = false, length = 36)
	public java.lang.String getId() {
		return this.id;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String id
	 */
	public void setId(java.lang.String id) {
		this.id = id;
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 场景
	 */

	@Column(name = "SCENE_BY", nullable = false, length = 36)
	public java.lang.String getSceneBy() {
		return this.sceneBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String 场景
	 */
	public void setSceneBy(java.lang.String sceneBy) {
		this.sceneBy = sceneBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 设备
	 */

	@Column(name = "DEVICE_BY", nullable = false, length = 36)
	public java.lang.String getDeviceBy() {
		return this.deviceBy;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String 设备
	 */
	public void setDeviceBy(java.lang.String deviceBy) {
		this.deviceBy = deviceBy;
	}

	/**
	 * 取得父级设备
	 *
	 * @return
	 */
	@Column(name = "DEVICE_PARENT_BY", nullable = false, length = 36)
	public java.lang.String getDeviceParentBy() {
		return deviceParentBy;
	}

	/**
	 * 设置父级设备
	 *
	 * @param deviceParentBy
	 */
	public void setDeviceParentBy(java.lang.String deviceParentBy) {
		this.deviceParentBy = deviceParentBy;
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 设备编码
	 */

	@Column(name = "DEVICE_CODE", nullable = false, length = 10)
	public java.lang.String getDeviceCode() {
		return this.deviceCode;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String 设备编码
	 */
	public void setDeviceCode(java.lang.String deviceCode) {
		this.deviceCode = deviceCode;
	}

	/**
	 * 取得设备地址
	 *
	 * @return
	 */
	@Column(name = "DEVICE_ADDRESS", nullable = false, length = 15)
	public java.lang.String getDeviceAddress() {
		return deviceAddress;
	}

	/**
	 * 设置设备地址
	 *
	 * @param deviceAddress
	 */
	public void setDeviceAddress(java.lang.String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	/**
	 * 取得是否请求命令
	 *
	 * @return
	 */
	@Column(name = "IS_SEND_CMD", nullable = false, length = 2)
	public String getIsSendCmd() {
		return isSendCmd;
	}

	/**
	 * 设置是否取得命令
	 *
	 * @param isSendCmd
	 */
	public void setIsSendCmd(String isSendCmd) {
		this.isSendCmd = isSendCmd;
	}


	/**
	 * 方法： 取得 请求命令数据类型
	 *
	 * @return
	 */
	@Column(name = "CMD_DATA_TYPE", nullable = false, length = 10)
	public String getCmdDataType() {
		return cmdDataType;
	}

	/**
	 * 方法： 设置 请求命令数据类型
	 *
	 * @param cmdDataType
	 */
	public void setCmdDataType(String cmdDataType) {
		this.cmdDataType = cmdDataType;
	}


	/**
	 * 取得请求命令
	 *
	 * @return
	 */
	@Column(name = "REQUEST_CMD", nullable = false, length = 50)
	public java.lang.String getRequestCmd() {
		return requestCmd;
	}

	/**
	 * 设置请求命令
	 *
	 * @param requestCmd
	 */
	public void setRequestCmd(java.lang.String requestCmd) {
		this.requestCmd = requestCmd;
	}

	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 设备位置
	 */

	@Column(name = "THREE_DATA", nullable = false, length = 200)
	public java.lang.String getThreeData() {
		return this.threeData;
	}

	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String 设备位置
	 */
	public void setThreeData(java.lang.String threeData) {
		this.threeData = threeData;
	}

	@Override
	public String toString() {
		return "SceneDeviceDepolyEntity{" +
				"id='" + id + '\'' +
				", sceneBy='" + sceneBy + '\'' +
				", deviceBy='" + deviceBy + '\'' +
				", deviceParentBy='" + deviceParentBy + '\'' +
				", deviceCode='" + deviceCode + '\'' +
				", deviceAddress='" + deviceAddress + '\'' +
				", isSendCmd='" + isSendCmd + '\'' +
				", cmdDataType='" + cmdDataType + '\'' +
				", requestCmd='" + requestCmd + '\'' +
				", threeData='" + threeData + '\'' +
				'}';
	}

}