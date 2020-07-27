package com.jeecg.protocol.service;
import com.jeecg.protocol.entity.ComCheckEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface ComCheckServiceI extends CommonService{
	
 	public void delete(ComCheckEntity entity) throws Exception;
 	
 	public Serializable save(ComCheckEntity entity) throws Exception;
 	
 	public void saveOrUpdate(ComCheckEntity entity) throws Exception;
 	
}
