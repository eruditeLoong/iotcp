package com.jeecg.device.service.impl;

import com.jeecg.data.entity.IotDataEntity;
import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.device.page.BaseDevicePage;
import com.jeecg.device.service.BaseDeviceServiceI;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.entity.SceneDeviceEntity;
import com.jeecg.scene.entity.SceneEntity;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("baseDeviceService")
@Transactional
public class BaseDeviceServiceImpl extends CommonServiceImpl implements BaseDeviceServiceI {

	@Autowired
	private SystemService systemService;

	/**
	 * 判断是否允许删除
	 *
	 * @param entity
	 * @return 返回为空时可删除
	 */
	@Override
	public String isAllowDel(BaseDeviceEntity entity) {
		// 1、设备配置
		List<SceneDeviceEntity> list1 = systemService.findByProperty(SceneDeviceEntity.class, "deviceBy", entity.getId());
		if (list1.size() > 0) return "与设备配置信息有关联，不可删除";
		// 2、部署设备
		List<SceneDeviceDepolyEntity> list2 = systemService.findByProperty(SceneDeviceDepolyEntity.class, "deviceBy", entity.getId());
		if (list2.size() > 0) return "与设备部署信息有关联，不可删除";
		// 3、部署设备-父级
		List<SceneDeviceDepolyEntity> list3 = systemService.findByProperty(SceneDeviceDepolyEntity.class, "deviceParentBy", entity.getId());
		if (list3.size() > 0) return "与设备部署信息有关联，不可删除";
		return null;
	}

	/**
	 * 获取折线数据
	 * @param deployDeviceId 部署设备id
	 * @param fieldBy 数据量
	 * @return
	 */
	public List<Map<String, Object>> listLineData(String deployDeviceId, Date createDate, String fieldBy){
		StringBuffer sql = new StringBuffer("SELECT create_date name, data value FROM jform_iot_data where instance_device_by=? and field_by=?");
		if(createDate!=null) {
			sql.append(" and create_date>'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createDate.getTime())+"'");
		}
		List<Map<String, Object>> list = systemService.findForJdbc(sql.toString(), deployDeviceId, fieldBy);
		return list;
	}
	
	/**
	 * 获取饼图数据
	 * 
	 * @param deployDeviceId 部署设备id
	 * @param fieldBy        数据量
	 * @return Map<String, Object>
	 */
	public List<Map<String, Object>> listPieData(String deployDeviceId, Date createDate, String fieldBy) {
		StringBuffer sql = new StringBuffer("select t.typename name,dd.status, dd.value from  t_s_type t, t_s_typegroup g, (SELECT d.field_by field_by, d.status status, count(1) value FROM jform_iot_data d where instance_device_by=? and field_by=? ");
		if(createDate!=null) {
			sql.append(" and d.create_date>'"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createDate.getTime())+"'");
		}
		sql.append(" group by d.status)dd where t.typegroupid=g.id and g.typegroupcode='datCountTy' and dd.status=t.typecode");
		List<Map<String, Object>> list = systemService.findForJdbc(sql.toString(), deployDeviceId, fieldBy);
		return list;
	}

	/**
	 * 获取设备的数据属性
	 * 
	 * @param deviceId    设备id
	 * @param deviceField 设备field
	 * @return BaseDeviceDataEntity
	 */
	public BaseDeviceDataEntity getDeviceData(String deviceId, String deviceField) {
		String hql = "from BaseDeviceDataEntity where deviceBy=? and field=?";
		List<BaseDeviceDataEntity> list = systemService.findHql(hql, deviceId, deviceField);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 获取设备属性列表
	 * 
	 * @param deviceId 设备id
	 * @return List<BaseDeviceDataEntity>
	 */
	public List<BaseDeviceDataEntity> listDeviceData(String deviceId) {
		String hql = "from BaseDeviceDataEntity where deviceBy=?";
		List<BaseDeviceDataEntity> list = systemService.findHql(hql, deviceId);
		return list;
	}

	public void delete(BaseDeviceEntity entity) throws Exception {
		// 执行删除文件
		String path = entity.getModelFile();
		systemService.delFileByPath(path);
		super.delete(entity);
	}

	public void addMain(BaseDevicePage baseDevicePage) throws Exception {
		BaseDeviceEntity baseDevice = new BaseDeviceEntity();
		MyBeanUtils.copyBeanNotNull2Bean(baseDevicePage, baseDevice);
		// 保存主信息
		this.save(baseDevice);
		/** 保存-基础设备数据点 */
		List<BaseDeviceDataEntity> baseDeviceDataList = baseDevicePage.getBaseDeviceDataList();
		for (BaseDeviceDataEntity baseDeviceData : baseDeviceDataList) {
			// 外键设置
			baseDeviceData.setDeviceBy(baseDevice.getId());
			this.save(baseDeviceData);
		}
	}

	public void updateMain(BaseDevicePage baseDevicePage) throws Exception {
		BaseDeviceEntity baseDevice = new BaseDeviceEntity();
		// 保存主表信息
		if (StringUtil.isNotEmpty(baseDevicePage.getId())) {
			baseDevice = findUniqueByProperty(BaseDeviceEntity.class, "id", baseDevicePage.getId());
			MyBeanUtils.copyBeanNotNull2Bean(baseDevicePage, baseDevice);
			this.saveOrUpdate(baseDevice);
		} else {
			this.saveOrUpdate(baseDevice);
		}
		// ===================================================================================
		// 获取参数
		Object deviceBy0 = baseDevice.getId();
		// ===================================================================================
		// 1.基础设备数据点数据库的数据
		String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
		List<BaseDeviceDataEntity> baseDeviceDataOldList = this.findHql(hql0, deviceBy0);
		// 2.基础设备数据点新的数据
		List<BaseDeviceDataEntity> baseDeviceDataList = baseDevicePage.getBaseDeviceDataList();
		// 3.筛选更新明细数据-基础设备数据点
		if (baseDeviceDataList != null && baseDeviceDataList.size() > 0) {
			for (BaseDeviceDataEntity oldE : baseDeviceDataOldList) {
				boolean isUpdate = false;
				for (BaseDeviceDataEntity sendE : baseDeviceDataList) {
					// 需要更新的明细数据-基础设备数据点
					if (oldE.getId().equals(sendE.getId())) {
						try {
							MyBeanUtils.copyBeanNotNull2Bean(sendE, oldE);
							this.saveOrUpdate(oldE);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(e.getMessage());
						}
						isUpdate = true;
						break;
					}
				}
				if (!isUpdate) {
					// 如果数据库存在的明细，前台没有传递过来则是删除-基础设备数据点
					super.delete(oldE);
				}

			}
			// 4.持久化新增的数据-基础设备数据点
			for (BaseDeviceDataEntity baseDeviceData : baseDeviceDataList) {
				if (oConvertUtils.isEmpty(baseDeviceData.getId())) {
					// 外键设置
					baseDeviceData.setDeviceBy(baseDevice.getId());
					this.save(baseDeviceData);
				}
			}
		}
	}

	public void delMain(BaseDeviceEntity baseDevice) throws Exception {
		// 执行删除文件
		String path = baseDevice.getModelFile();
		systemService.delFileByPath(path);
		// 删除主表信息
		this.delete(baseDevice);
		// ===================================================================================
		// 获取参数
		Object deviceBy0 = baseDevice.getId();
		// ===================================================================================
		// 删除-基础设备数据点
		String hql0 = "from BaseDeviceDataEntity where 1 = 1 AND deviceBy = ? ";
		List<BaseDeviceDataEntity> baseDeviceDataOldList = this.findHql(hql0, deviceBy0);
		this.deleteAllEntitie(baseDeviceDataOldList);
	}

}