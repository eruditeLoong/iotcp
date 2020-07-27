package com.jeecg.protocol.service;
import com.jeecg.protocol.entity.ComProtocolEntity;
import com.jeecg.protocol.page.ComProtocolPage;
import com.jeecg.protocol.entity.ComConfigEntity;

import java.util.List;
import org.jeecgframework.core.common.service.CommonService;
import java.io.Serializable;

public interface ComProtocolServiceI extends CommonService{
 	public void delete(ComProtocolEntity entity) throws Exception;
	/**
	 * 添加一对多
	 * 
	 */
	public void addMain(ComProtocolPage comProtocolPage) throws Exception;
	        
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(ComProtocolPage comProtocolPage) throws Exception;
	
	/**
	 * 删除一对多
	 * 
	 */
	public void delMain (ComProtocolEntity comProtocol) throws Exception;
}
