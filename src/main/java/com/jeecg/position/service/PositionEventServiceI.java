package com.jeecg.position.service;
import com.jeecg.position.entity.PositionEventEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PositionEventServiceI extends CommonService{
	
 	public void delete(PositionEventEntity entity) throws Exception;
 	
 	public Serializable save(PositionEventEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PositionEventEntity entity) throws Exception;
 	
}
