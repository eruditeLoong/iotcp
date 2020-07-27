package com.jeecg.protocol.service;
import com.jeecg.protocol.entity.NetworkSchemeEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface NetworkSchemeServiceI extends CommonService{
	
 	public void delete(NetworkSchemeEntity entity) throws Exception;
 	
 	public Serializable save(NetworkSchemeEntity entity) throws Exception;
 	
 	public void saveOrUpdate(NetworkSchemeEntity entity) throws Exception;
 	
}
