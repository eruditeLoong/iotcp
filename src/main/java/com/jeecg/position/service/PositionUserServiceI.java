package com.jeecg.position.service;
import com.jeecg.position.entity.PositionUserEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PositionUserServiceI extends CommonService{
	
 	public void delete(PositionUserEntity entity) throws Exception;
 	
 	public Serializable save(PositionUserEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PositionUserEntity entity) throws Exception;
 	
}
