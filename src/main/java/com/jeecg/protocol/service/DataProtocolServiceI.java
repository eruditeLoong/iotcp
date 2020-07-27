package com.jeecg.protocol.service;
import com.jeecg.protocol.entity.DataProtocolEntity;
import com.jeecg.protocol.page.DataProtocolPage;
import com.jeecg.protocol.entity.DataCustomEntity;

import java.util.List;
import org.jeecgframework.core.common.service.CommonService;
import java.io.Serializable;

public interface DataProtocolServiceI extends CommonService{
 	public void delete(DataProtocolEntity entity) throws Exception;
	/**
	 * 添加一对多
	 * 
	 */
	public void addMain(DataProtocolPage dataProtocolPage) throws Exception;
	        
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(DataProtocolPage dataProtocolPage) throws Exception;
	
	/**
	 * 删除一对多
	 * 
	 */
	public void delMain (DataProtocolEntity dataProtocol) throws Exception;
}
