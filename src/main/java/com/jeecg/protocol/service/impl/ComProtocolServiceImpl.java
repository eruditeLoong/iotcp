package com.jeecg.protocol.service.impl;

import java.util.List;

import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeecg.protocol.entity.ComConfigEntity;
import com.jeecg.protocol.entity.ComProtocolEntity;
import com.jeecg.protocol.page.ComProtocolPage;
import com.jeecg.protocol.service.ComProtocolServiceI;

@Service("comProtocolService")
@Transactional
public class ComProtocolServiceImpl extends CommonServiceImpl implements ComProtocolServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public void delete(ComProtocolEntity entity) throws Exception {
		super.delete(entity);
	}

	public void addMain(ComProtocolPage comProtocolPage) throws Exception {
		ComProtocolEntity comProtocol = new ComProtocolEntity();
		MyBeanUtils.copyBeanNotNull2Bean(comProtocolPage, comProtocol);
		// 保存主信息
		this.save(comProtocol);
		/** 保存-协议配置 */
		List<ComConfigEntity> comConfigList = comProtocolPage.getComConfigList();
		for (ComConfigEntity comConfig : comConfigList) {
			// 外键设置
			comConfig.setComProtocolBy(comProtocol.getId());
			this.save(comConfig);
		}
	}

	public void updateMain(ComProtocolPage comProtocolPage) throws Exception {
		ComProtocolEntity comProtocol = new ComProtocolEntity();
		// 保存主表信息
		if (StringUtil.isNotEmpty(comProtocolPage.getId())) {
			comProtocol = findUniqueByProperty(ComProtocolEntity.class, "id", comProtocolPage.getId());
			MyBeanUtils.copyBeanNotNull2Bean(comProtocolPage, comProtocol);
			this.saveOrUpdate(comProtocol);
		} else {
			this.saveOrUpdate(comProtocol);
		}
		// ===================================================================================
		// 获取参数
		Object comProtocolBy0 = comProtocol.getId();
		// ===================================================================================
		// 1.协议配置数据库的数据
		String hql0 = "from ComConfigEntity where 1 = 1 AND comProtocolBy = ? ";
		List<ComConfigEntity> comConfigOldList = this.findHql(hql0, comProtocolBy0);
		// 2.协议配置新的数据
		List<ComConfigEntity> comConfigList = comProtocolPage.getComConfigList();
		// 3.筛选更新明细数据-协议配置
		if (comConfigList != null && comConfigList.size() > 0) {
			for (ComConfigEntity oldE : comConfigOldList) {
				boolean isUpdate = false;
				for (ComConfigEntity sendE : comConfigList) {
					// 需要更新的明细数据-协议配置
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
					// 如果数据库存在的明细，前台没有传递过来则是删除-协议配置
					super.delete(oldE);
				}

			}
			// 4.持久化新增的数据-协议配置
			for (ComConfigEntity comConfig : comConfigList) {
				if (oConvertUtils.isEmpty(comConfig.getId())) {
					// 外键设置
					comConfig.setComProtocolBy(comProtocol.getId());
					this.save(comConfig);
				}
			}
		}
	}

	public void delMain(ComProtocolEntity comProtocol) throws Exception {
		// 删除主表信息
		this.delete(comProtocol);
		// ===================================================================================
		// 获取参数
		Object comProtocolBy0 = comProtocol.getId();
		// ===================================================================================
		// 删除-协议配置
		String hql0 = "from ComConfigEntity where 1 = 1 AND comProtocolBy = ? ";
		List<ComConfigEntity> comConfigOldList = this.findHql(hql0, comProtocolBy0);
		this.deleteAllEntitie(comConfigOldList);
	}

}