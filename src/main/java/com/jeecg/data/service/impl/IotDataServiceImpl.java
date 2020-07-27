package com.jeecg.data.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecg.data.entity.IotDataEntity;
import com.jeecg.data.service.IotDataServiceI;
import com.jeecg.device.entity.BaseDeviceDataEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.service.SceneDeviceDepolyServiceI;

@Service("iotDataService")
@Transactional
public class IotDataServiceImpl extends CommonServiceImpl implements IotDataServiceI {
	@Autowired
	private SystemService systemService;
	@Autowired
	private SceneDeviceDepolyServiceI sceneDeviceDepolyService;

	/**
	 * 计算数据状态
	 * 
	 * @param entity
	 * @return Integer
	 */
	public Integer getDataStatus(IotDataEntity entity) throws Exception {
		Integer status = -1;
		if (StringUtil.isBlank(entity.getInstanceDeviceBy())) {
			throw new BusinessException("部署设备id不能为空！");
		}
		// 根据部署设备id拿到部署信息
		SceneDeviceDepolyEntity sdd = systemService.get(SceneDeviceDepolyEntity.class, entity.getInstanceDeviceBy());
		List<BaseDeviceDataEntity> bddList = systemService.findHql("FROM BaseDeviceDataEntity WHERE 1=1 AND deviceBy=? AND field=?", sdd.getDeviceBy(), entity.getFieldBy());
		if (bddList != null && bddList.size() > 0) {
			// 设备数据
			BaseDeviceDataEntity bdd = bddList.get(0);
			// 取值范围
			String dr = bdd.getNormalDataRange();
			// 判断正常数值范围
			Float d = Float.valueOf(entity.getData());
			if (d < Float.valueOf(dr.split(",")[0])) {
				status = 0; // 低值
			} else if (d > Float.valueOf(dr.split(",")[1])) {
				status = 2; // 超值
			} else {
				status = 1; // 正常值
			}
		} else {
			throw new BusinessException("基础设备信息！");
		}
		return status;
	}

	public void delete(IotDataEntity entity) throws Exception {
		super.delete(entity);
	}

	public Serializable save(IotDataEntity entity) throws Exception {
		Serializable t = super.save(entity);
		return t;
	}

	public void saveOrUpdate(IotDataEntity entity) throws Exception {
		super.saveOrUpdate(entity);
	}

	@Override
	public Map<String, Map<String, Object>> apendParams(DataGrid dataGrid) throws Exception {
		Map<String, Map<String, Object>> extMap = new HashMap<String, Map<String, Object>>();
		@SuppressWarnings("unchecked")
		List<IotDataEntity> list = (List<IotDataEntity>) dataGrid.getResults();
		for (IotDataEntity d : list) {
			Map<String, Object> m = new HashMap<String, Object>();
			SceneDeviceDepolyEntity sdd = systemService.get(SceneDeviceDepolyEntity.class, d.getInstanceDeviceBy());
			m.put("sceneBy", sdd.getSceneBy());
			m.put("deviceBy", sdd.getDeviceBy());
			m.put("deviceCode", sdd.getDeviceCode());
			extMap.put(d.getId(), m);
		}
		return extMap;
	}

}