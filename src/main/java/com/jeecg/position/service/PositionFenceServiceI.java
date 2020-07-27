package com.jeecg.position.service;
import com.jeecg.position.entity.PositionFenceEntity;
import com.jeecg.position.page.PositionFencePage;

import org.jeecgframework.core.common.service.CommonService;

public interface PositionFenceServiceI extends CommonService{
 	public void delete(PositionFenceEntity entity) throws Exception;
	/**
	 * 添加一对多
	 * 
	 */
	public void addMain(PositionFencePage positionFencePage) throws Exception;
	        
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(PositionFencePage positionFencePage) throws Exception;
	
	/**
	 * 删除一对多
	 * 
	 */
	public void delMain (PositionFenceEntity positionFence) throws Exception;
}
