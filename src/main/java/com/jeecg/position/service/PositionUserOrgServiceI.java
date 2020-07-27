package com.jeecg.position.service;
import com.jeecg.position.entity.PositionUserOrgEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface PositionUserOrgServiceI extends CommonService{
	
 	public void delete(PositionUserOrgEntity entity) throws Exception;
 	
 	public Serializable save(PositionUserOrgEntity entity) throws Exception;
 	
 	public void saveOrUpdate(PositionUserOrgEntity entity) throws Exception;
 	
}
