package com.jeecg.scene.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.GenericsUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeecg.device.entity.BaseDeviceEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.service.SceneDeviceDepolyServiceI;

@Service("sceneDeviceDepolyService")
@Transactional
public class SceneDeviceDepolyServiceImpl extends CommonServiceImpl implements SceneDeviceDepolyServiceI {

	@Autowired
	private SystemService systemService;

	public void delete(SceneDeviceDepolyEntity entity) throws Exception {
		super.delete(entity);
	}

	public Serializable save(SceneDeviceDepolyEntity entity) throws Exception {
		Serializable t = super.save(entity);
		return t;
	}

	public void saveOrUpdate(SceneDeviceDepolyEntity entity) throws Exception {
		super.saveOrUpdate(entity);
	}

	public JSONArray getDeviceTreeList(String sceneBy) {
		JSONArray treeList = new JSONArray();
		List<SceneDeviceDepolyEntity> list = systemService.findByProperty(SceneDeviceDepolyEntity.class, "sceneBy", sceneBy);
		Map<String, JSONArray> treeMap = new HashMap<>();
		for (SceneDeviceDepolyEntity sdd : list) {
			JSONObject tree = new JSONObject();
			tree.put("id", sdd.getId());
			tree.put("pid", StringUtil.isBlank(sdd.getDeviceParentBy()) ? "" : sdd.getDeviceParentBy());
			BaseDeviceEntity bd = systemService.get(BaseDeviceEntity.class, sdd.getDeviceBy());
			tree.put("name", bd.getName() + "[" + sdd.getDeviceCode() + "]");
			tree.put("deviceDeploy", JSONObject.toJSON(sdd.getThreeData()));
			treeList.add(tree);
		}
		return treeList;
	}
}