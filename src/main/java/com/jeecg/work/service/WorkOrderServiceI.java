package com.jeecg.work.service;
import com.jeecg.work.entity.WorkOrderEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface WorkOrderServiceI extends CommonService{
	
 	public void delete(WorkOrderEntity entity) throws Exception;
 	
 	public Serializable save(WorkOrderEntity entity) throws Exception;
 	
 	public void saveOrUpdate(WorkOrderEntity entity) throws Exception;
 	
}
