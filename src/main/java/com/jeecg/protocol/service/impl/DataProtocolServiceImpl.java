package com.jeecg.protocol.service.impl;

import com.jeecg.protocol.service.DataProtocolServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import com.jeecg.protocol.entity.DataProtocolEntity;
import com.jeecg.protocol.page.DataProtocolPage;
import com.jeecg.protocol.entity.DataCustomEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import java.util.ArrayList;
import java.util.UUID;
import java.io.Serializable;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.minidao.util.FreemarkerParseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.jeecgframework.core.util.ResourceUtil;

@Service("dataProtocolService")
@Transactional
public class DataProtocolServiceImpl extends CommonServiceImpl implements DataProtocolServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void delete(DataProtocolEntity entity) throws Exception {
		super.delete(entity);
	}

	public void addMain(DataProtocolPage dataProtocolPage) throws Exception {
		DataProtocolEntity dataProtocol = new DataProtocolEntity();
		MyBeanUtils.copyBeanNotNull2Bean(dataProtocolPage, dataProtocol);
		// 保存主信息
		this.save(dataProtocol);
		/** 保存-自定义格式 */
		List<DataCustomEntity> dataCustomList = dataProtocolPage.getDataCustomList();
		for (DataCustomEntity dataCustom : dataCustomList) {
			// 外键设置
			dataCustom.setProtocolBy(dataProtocol.getId());
			this.save(dataCustom);
		}
	}

	public void updateMain(DataProtocolPage dataProtocolPage) throws Exception {
		DataProtocolEntity dataProtocol = new DataProtocolEntity();
		// 保存主表信息
		if (StringUtil.isNotEmpty(dataProtocolPage.getId())) {
			dataProtocol = findUniqueByProperty(DataProtocolEntity.class, "id", dataProtocolPage.getId());
			MyBeanUtils.copyBeanNotNull2Bean(dataProtocolPage, dataProtocol);
			this.saveOrUpdate(dataProtocol);
		} else {
			this.saveOrUpdate(dataProtocol);
		}
		// ===================================================================================
		// 获取参数
		Object protocolBy0 = dataProtocol.getId();
		// ===================================================================================
		// 1.自定义格式数据库的数据
		String hql0 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? ";
		List<DataCustomEntity> dataCustomOldList = this.findHql(hql0, protocolBy0);
		// 2.自定义格式新的数据
		List<DataCustomEntity> dataCustomList = dataProtocolPage.getDataCustomList();
		// 3.筛选更新明细数据-自定义格式
		if (dataCustomList != null && dataCustomList.size() > 0) {
			for (DataCustomEntity oldE : dataCustomOldList) {
				boolean isUpdate = false;
				for (DataCustomEntity sendE : dataCustomList) {
					// 需要更新的明细数据-自定义格式
					if (oldE.getId().equals(sendE.getId())) {
						try {
							MyBeanUtils.copyBeanNotNull2Bean(sendE, oldE);
							this.saveOrUpdate(oldE);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(e.getMessage());
						}
						isUpdate = true;
						break;
					}
				}
				if (!isUpdate) {
					// 如果数据库存在的明细，前台没有传递过来则是删除-自定义格式
					super.delete(oldE);
				}

			}
			// 4.持久化新增的数据-自定义格式
			for (DataCustomEntity dataCustom : dataCustomList) {
				if (oConvertUtils.isEmpty(dataCustom.getId())) {
					// 外键设置
					dataCustom.setProtocolBy(dataProtocol.getId());
					this.save(dataCustom);
				}
			}
		}
	}

	public void delMain(DataProtocolEntity dataProtocol) throws Exception {
		// 删除主表信息
		this.delete(dataProtocol);
		// ===================================================================================
		// 获取参数
		Object protocolBy0 = dataProtocol.getId();
		// ===================================================================================
		// 删除-自定义格式
		String hql0 = "from DataCustomEntity where 1 = 1 AND protocolBy = ? ";
		List<DataCustomEntity> dataCustomOldList = this.findHql(hql0, protocolBy0);
		this.deleteAllEntitie(dataCustomOldList);
	}

}