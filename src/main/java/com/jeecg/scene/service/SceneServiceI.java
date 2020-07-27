package com.jeecg.scene.service;

import com.jeecg.scene.entity.SceneEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface SceneServiceI extends CommonService {
	/**
	 * 判断是否允许删除
	 *
	 * @param entity
	 * @return 返回为空时可删除
	 */
	public String isAllowDel(SceneEntity entity);

	public void delete(SceneEntity entity) throws Exception;

	public Serializable save(SceneEntity entity) throws Exception;

	public void saveOrUpdate(SceneEntity entity) throws Exception;

}
