package com.jeecg.device.service;
import com.jeecg.device.entity.InstanceDeviceEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface InstanceDeviceServiceI extends CommonService{
	
 	public void delete(InstanceDeviceEntity entity) throws Exception;
 	
 	public Serializable save(InstanceDeviceEntity entity) throws Exception;
 	
 	public void saveOrUpdate(InstanceDeviceEntity entity) throws Exception;
 	
}
