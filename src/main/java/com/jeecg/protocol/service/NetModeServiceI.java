package com.jeecg.protocol.service;
import com.jeecg.protocol.entity.NetModeEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface NetModeServiceI extends CommonService{
	
 	public void delete(NetModeEntity entity) throws Exception;
 	
 	public Serializable save(NetModeEntity entity) throws Exception;
 	
 	public void saveOrUpdate(NetModeEntity entity) throws Exception;
 	
}
