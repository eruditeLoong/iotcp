package com.jeecg.position.service.impl;

import com.jeecg.position.entity.PositionModuleEntity;
import com.jeecg.position.service.PositionModuleServiceI;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.HexConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("positionModuleService")
@Transactional
public class PositionModuleServiceImpl extends CommonServiceImpl implements PositionModuleServiceI {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void delete(PositionModuleEntity entity) throws Exception {
        super.delete(entity);
    }

    public Serializable save(PositionModuleEntity entity) throws Exception {
        Serializable t = super.save(entity);
        return t;
    }

    public void saveOrUpdate(PositionModuleEntity entity) throws Exception {
        super.saveOrUpdate(entity);
    }

    @Override
    public void manualPosition(IoSession session) {
        byte[] bMPosition = HexConvertUtil.hexStringToBytes("78 78 01 80 0D 0A");
        session.write(IoBuffer.wrap(bMPosition));
    }

    @Override
    public Map<String, Map<String, Object>> apendParams(DataGrid dataGrid) {
        Map<String, Map<String, Object>> extMap = new HashMap<String, Map<String, Object>>();
        List<PositionModuleEntity> list = (List<PositionModuleEntity>) dataGrid.getResults();
        for (PositionModuleEntity temp : list) {
            Map<String, Object> m = new HashMap<String, Object>();
            extMap.put(temp.getId(), m);
        }
        return extMap;
    }

}