package com.jeecg.position.service;

import com.jeecg.position.entity.PositionModuleEntity;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;
import java.util.Map;

public interface PositionModuleServiceI extends CommonService {

    public void delete(PositionModuleEntity entity) throws Exception;

    public Serializable save(PositionModuleEntity entity) throws Exception;

    public void saveOrUpdate(PositionModuleEntity entity) throws Exception;

    public void manualPosition(IoSession session);

    public Map<String, Map<String, Object>> apendParams(DataGrid dataGrid);
}
