package com.jeecg.device.service;
import com.jeecg.device.entity.NschemeDeviceEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface NschemeDeviceServiceI extends CommonService{
	
 	public void delete(NschemeDeviceEntity entity) throws Exception;
 	
 	public Serializable save(NschemeDeviceEntity entity) throws Exception;
 	
 	public void saveOrUpdate(NschemeDeviceEntity entity) throws Exception;
 	
}
