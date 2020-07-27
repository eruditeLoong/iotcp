package com.jeecg.scene.service.impl;

import com.jeecg.data.entity.IotDataEntity;
import com.jeecg.scene.entity.SceneDeviceDepolyEntity;
import com.jeecg.scene.entity.SceneDeviceEntity;
import com.jeecg.scene.entity.SceneEntity;
import com.jeecg.scene.service.SceneServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service("sceneService")
@Transactional
public class SceneServiceImpl extends CommonServiceImpl implements SceneServiceI {
	@Autowired
	private SystemService systemService;

	/**
	 * 判断是否允许删除
	 *
	 * @param entity
	 * @return 返回为空时可删除
	 */
	@Override
	public String isAllowDel(SceneEntity entity) {
		// 1、设备配置
		List<SceneDeviceEntity> list1 = systemService.findByProperty(SceneDeviceEntity.class, "sceneBy", entity.getId());
		if (list1.size() > 0) return "场景下有设备配置信息，不可删除";
		// 2、部署设备
		List<SceneDeviceDepolyEntity> list2 = systemService.findByProperty(SceneDeviceDepolyEntity.class, "sceneBy", entity.getId());
		if (list2.size() > 0) return "场景下有设备部署信息，不可删除";
		// 3、数据
		List<IotDataEntity> list3 = systemService.findByProperty(IotDataEntity.class, "instanceDeviceBy", entity.getId());
		if (list3.size() > 0) return "场景下有设备数据，不可删除";
		return null;
	}

	public void delete(SceneEntity entity) throws Exception {
		// 执行删除文件
		systemService.delFileByPath(entity.getScene2d());
		systemService.delFileByPath(entity.getScene3d());
		super.delete(entity);
	}

	public Serializable save(SceneEntity entity) throws Exception {
		Serializable t = super.save(entity);
		return t;
	}

	public void saveOrUpdate(SceneEntity entity) throws Exception {
		super.saveOrUpdate(entity);
	}

}