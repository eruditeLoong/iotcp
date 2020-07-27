package com.jeecg.data.service;

import java.io.Serializable;
import java.util.Map;

import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;

import com.jeecg.data.entity.IotDataEntity;

public interface IotDataServiceI extends CommonService {

	/**
	 * 计算数据状态
	 * 
	 * @param entity
	 * @return Integer
	 */
	public Integer getDataStatus(IotDataEntity entity) throws Exception;

	public void delete(IotDataEntity entity) throws Exception;

	public Serializable save(IotDataEntity entity) throws Exception;

	public void saveOrUpdate(IotDataEntity entity) throws Exception;
	
	/**
	 * @title 追加变量
	 * @author 周文荣 
	 * @time 2018/07/20 19:25
	 * @param dataGrid
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Object>> apendParams(DataGrid dataGrid) throws Exception;

}
