package com.jeecg.position.service.impl;

import com.jeecg.position.entity.PositionTrajectoryEntity;
import com.jeecg.position.service.PositionTrajectoryServiceI;
import com.jeecg.position.utils.GPSFormat;
import net.sf.json.JSONObject;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("positionTrajectoryService")
@Transactional
public class PositionTrajectoryServiceImpl extends CommonServiceImpl implements PositionTrajectoryServiceI {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static double[] MillierConvertion(double lon, double lat) {
        double L = 6381372 * Math.PI * 2;//地球周长
        double W = L;// 平面展开后，x轴等于周长
        double H = L / 2;// y轴约等于周长一半
        double mill = 2.3;// 米勒投影中的一个常数，范围大约在正负2.3之间
        double x = lon * Math.PI / 180;// 将经度从度数转换为弧度
        double y = lat * Math.PI / 180;// 将纬度从度数转换为弧度
        y = 1.25 * Math.log(Math.tan(0.25 * Math.PI + 0.4 * y));// 米勒投影的转换
        // 弧度转为实际距离
        x = (W / 2) + (W / (2 * Math.PI)) * x;
        y = (H / 2) - (H / (2 * mill)) * y;
        double[] result = new double[2];
        result[0] = x;
        result[1] = y;
        return result;
    }

    public void delete(PositionTrajectoryEntity entity) throws Exception {
        super.delete(entity);
    }

    public Serializable save(PositionTrajectoryEntity entity) throws Exception {
        Serializable t = super.save(entity);
        return t;
    }

    public void saveOrUpdate(PositionTrajectoryEntity entity) throws Exception {
        super.saveOrUpdate(entity);
    }

    /**
     * 经纬度转换成threejs坐标
     *
     * @param lon 经度
     * @param lat 纬度
     * @return
     */
    @Override
    public double[] lonlat2three(double lon, double lat) {
        // 拿到默认场景的经纬度、缩放倍数
        List<Map<String, Object>> list = super.findForJdbc("select scale, position, three_data from jform_scene where 1=1 and is_default_view=?", 1);
        double[] res1 = {0.0, 0.0};
        double[] res2 = {0.0, 0.0};
        if (list.size() > 0) {
            String position = list.get(0).get("position").toString();
            String threeData = list.get(0).get("three_data").toString();
            Integer scale = (Integer) list.get(0).get("scale");
            JSONObject jThree = JSONObject.fromObject(threeData);
            Double sLon = Double.valueOf(position.split(",")[0]);
            Double sLat = Double.valueOf(position.split(",")[1]);
            double d = GPSFormat.distance(lon, lat, sLon, sLat);
            res1 = MillierConvertion(lon, lat);
            res2 = MillierConvertion(sLon, sLat);
            res1[0] = ((res1[0] - res2[0]) / scale) * jThree.getDouble("s");
            res1[1] = ((res1[1] - res2[1]) / scale) * jThree.getDouble("s");
        }
        return res1;
    }

    @Override
    public List<Point2D.Double> getDefaultSceneFence() {
        // 1、边界计算，超出边界不显示
        String sql = "select p.point from position_fence f left join jform_scene s on f.scene_by=s.id left join position_fence_point p on p.fence_by=f.id " +
                "where 1=1 and s.is_default_view=1 and f.is_scene_boundary=1";
        List<Map<String, Object>> list = this.findForJdbc(sql);
        List<Point2D.Double> points = new ArrayList<>();
        for (Map<String, Object> map : list) {
            String[] pArr = map.get("point").toString().split(",");
            Point2D.Double p = new Point2D.Double(Double.valueOf(pArr[0]), Double.valueOf(pArr[2]));
            points.add(p);
        }
        return points;
    }

    public static void main(String[] args) {
        double d = GPSFormat.distance(117.07288588911173, 37.067760850450284
                , 117.07353740612976, 37.06793187179302);
        double[] res1 = MillierConvertion(117.07288588911173, 37.067760850450284);
        double[] res2 = MillierConvertion(117.07353740612976, 37.06793187179302);
        System.out.println(d);
        System.out.println(Arrays.toString(res1));
        System.out.println(Arrays.toString(res2));
        System.out.println(res1[0] - res2[0]);
        System.out.println(res1[1] - res2[1]);
    }
}