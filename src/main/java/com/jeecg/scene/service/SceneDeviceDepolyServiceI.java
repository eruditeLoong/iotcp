package com.jeecg.scene.service;

import java.io.Serializable;
import java.util.Map;

import org.jeecgframework.core.common.service.CommonService;

import com.alibaba.fastjson.JSONArray;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;

public interface SceneDeviceDepolyServiceI extends CommonService {

	public void delete(SceneDeviceDepolyEntity entity) throws Exception;

	public Serializable save(SceneDeviceDepolyEntity entity) throws Exception;

	public void saveOrUpdate(SceneDeviceDepolyEntity entity) throws Exception;

	/**
	 * 获取场景下所有部署设备的treeList
	 * 
	 * @author zhouwr
	 * @param sceneBy
	 * @return
	 */
	public JSONArray getDeviceTreeList(String sceneBy);
}
