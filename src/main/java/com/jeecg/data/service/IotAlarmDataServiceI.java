package com.jeecg.data.service;
import com.jeecg.data.entity.IotAlarmDataEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface IotAlarmDataServiceI extends CommonService{
	
 	public void delete(IotAlarmDataEntity entity) throws Exception;
 	
 	public Serializable save(IotAlarmDataEntity entity) throws Exception;
 	
 	public void saveOrUpdate(IotAlarmDataEntity entity) throws Exception;
 	
}
