package com.jeecg.position.service;

import com.jeecg.position.entity.PositionTrajectoryEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.List;

public interface PositionTrajectoryServiceI extends CommonService {

	public void delete(PositionTrajectoryEntity entity) throws Exception;

	public Serializable save(PositionTrajectoryEntity entity) throws Exception;

	public void saveOrUpdate(PositionTrajectoryEntity entity) throws Exception;

	/**
	 * 经纬度转换成threejs坐标
	 *
	 * @param longitude 经度
	 * @param latitude  纬度
	 * @return
	 */
	public double[] lonlat2three(double longitude, double latitude);

	/**
	 * 获取默认场景的边界顶点集合
	 * zhouwr
	 * 2019/10/16 14:30
	 *
	 * @return List<Point2D.Double>
	 */
	public List<Point2D.Double> getDefaultSceneFence();

	/**
	 * 判断移动速度
	 */
//	public Boolean moveSpeed();

}
