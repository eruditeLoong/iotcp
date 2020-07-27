package com.jeecg.scene.service;
import com.jeecg.scene.entity.SceneDeviceEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface SceneDeviceServiceI extends CommonService{
	
 	public void delete(SceneDeviceEntity entity) throws Exception;
 	
 	public Serializable save(SceneDeviceEntity entity) throws Exception;
 	
 	public void saveOrUpdate(SceneDeviceEntity entity) throws Exception;
 	
}
