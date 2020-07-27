package com.jeecg.work.service.impl;

import com.jeecg.work.entity.WorkOrderEntity;
import com.jeecg.work.service.WorkOrderServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Service("workOrderService")
@Transactional
public class WorkOrderServiceImpl extends CommonServiceImpl implements WorkOrderServiceI {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void delete(WorkOrderEntity entity) throws Exception {
        super.delete(entity);
    }

    public Serializable save(WorkOrderEntity entity) throws Exception {
        Serializable t = super.save(entity);
        return t;
    }

    public void saveOrUpdate(WorkOrderEntity entity) throws Exception {
        super.saveOrUpdate(entity);
    }

}