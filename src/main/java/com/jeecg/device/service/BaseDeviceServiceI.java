package com.jeecg.device.service;

import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.device.page.BaseDevicePage;
import com.jeecg.scene.entity.SceneEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BaseDeviceServiceI extends CommonService {

	/**
	 * 判断是否允许删除
	 *
	 * @param entity
	 * @return 返回为空时可删除
	 */
	public String isAllowDel(BaseDeviceEntity entity);

	/**
	 * 获取饼图数据
	 * @param deployDeviceId 部署设备id
	 * @param fieldBy 数据量
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> listPieData(String deployDeviceId, Date createDate, String fieldBy);

	/**
	 * 获取折线数据
	 * @param deployDeviceId 部署设备id
	 * @param fieldBy 数据量
	 * @return
	 */
	public List<Map<String, Object>> listLineData(String deployDeviceId, Date createDate, String fieldBy);

	/**
	 * 获取设备属性列表
	 * 
	 * @param deviceId 设备id
	 * @return List<BaseDeviceDataEntity>
	 */
	public List<BaseDeviceDataEntity> listDeviceData(String deviceId);

	/**
	 * 获取设备的数据属性
	 * 
	 * @param deviceId    设备id
	 * @param deviceField 设备field
	 * @return
	 */
	public BaseDeviceDataEntity getDeviceData(String deviceId, String deviceField);

	public void delete(BaseDeviceEntity entity) throws Exception;

	/**
	 * 添加一对多
	 * 
	 */
	public void addMain(BaseDevicePage baseDevicePage) throws Exception;

	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(BaseDevicePage baseDevicePage) throws Exception;

	/**
	 * 删除一对多
	 * 
	 */
	public void delMain(BaseDeviceEntity baseDevice) throws Exception;
}
