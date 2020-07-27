package com.jeecg.position.service.impl;

import com.jeecg.position.entity.PositionFenceEntity;
import com.jeecg.position.entity.PositionFencePointEntity;
import com.jeecg.position.page.PositionFencePage;
import com.jeecg.position.service.PositionFenceServiceI;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("positionFenceService")
@Transactional
public class PositionFenceServiceImpl extends CommonServiceImpl implements PositionFenceServiceI {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
 	public void delete(PositionFenceEntity entity) throws Exception{
 		super.delete(entity);
 	}
	
	public void addMain(PositionFencePage positionFencePage) throws Exception {
		PositionFenceEntity positionFence = new PositionFenceEntity();
		MyBeanUtils.copyBeanNotNull2Bean(positionFencePage, positionFence);
		//保存主信息
		this.save(positionFence);
		/**保存-围栏顶点集合*/
		List<PositionFencePointEntity> potionFencePointList = positionFencePage.getPotionFencePointList();
		for(PositionFencePointEntity potionFencePoint:potionFencePointList){
			//外键设置
			potionFencePoint.setFenceBy(positionFence.getId());
			this.save(potionFencePoint);
		}
	}

	
	public void updateMain(PositionFencePage positionFencePage) throws Exception{
		PositionFenceEntity positionFence = new PositionFenceEntity();
		//保存主表信息
		if(StringUtil.isNotEmpty(positionFencePage.getId())){
			positionFence = findUniqueByProperty(PositionFenceEntity.class, "id", positionFencePage.getId());
			MyBeanUtils.copyBeanNotNull2Bean(positionFencePage, positionFence);
			this.saveOrUpdate(positionFence);
		}else{
			this.saveOrUpdate(positionFence);
		}
		//===================================================================================
		//获取参数
		Object fenceBy0 = positionFence.getId();
		//===================================================================================
		//1.围栏顶点集合数据库的数据
	    List<PositionFencePointEntity> potionFencePointOldList = this.findByProperty(PositionFencePointEntity.class, "fenceBy",fenceBy0);
		//2.围栏顶点集合新的数据
		List<PositionFencePointEntity> potionFencePointList = positionFencePage.getPotionFencePointList();
	    //3.筛选更新明细数据-围栏顶点集合
		if(potionFencePointList!=null &&potionFencePointList.size()>0){
			for(PositionFencePointEntity oldE:potionFencePointOldList){
				boolean isUpdate = false;
				for(PositionFencePointEntity sendE:potionFencePointList){
					//需要更新的明细数据-围栏顶点集合
					if(oldE.getId().equals(sendE.getId())){
		    			try {
							MyBeanUtils.copyBeanNotNull2Bean(sendE,oldE);
							this.saveOrUpdate(oldE);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(e.getMessage());
						}
						isUpdate= true;
		    			break;
		    		}
		    	}
	    		if(!isUpdate){
		    		//如果数据库存在的明细，前台没有传递过来则是删除-围栏顶点集合
		    		super.delete(oldE);
	    		}
	    		
			}
			//4.持久化新增的数据-围栏顶点集合
			for(PositionFencePointEntity potionFencePoint:potionFencePointList){
				if(oConvertUtils.isEmpty(potionFencePoint.getId())){
					//外键设置
					potionFencePoint.setFenceBy(positionFence.getId());
					this.save(potionFencePoint);
				}
			}
		}
	}

	public void delMain(PositionFenceEntity positionFence) throws Exception{
		//删除主表信息
		this.delete(positionFence);
		//===================================================================================
		//获取参数
		Object fenceBy0 = positionFence.getId();
		//===================================================================================
		//删除-围栏顶点集合
		List<PositionFencePointEntity> potionFencePointOldList = this.findByProperty(PositionFencePointEntity.class, "fenceBy",fenceBy0);
		this.deleteAllEntitie(potionFencePointOldList);
	}


}