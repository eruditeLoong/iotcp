package com.jeecg.data.task;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.data.entity.IotDataEntity;
import com.jeecg.data.service.IotDataServiceI;
import org.jeecgframework.core.util.HttpRequest;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:AutoDataTask
 * @Description: 根据当地天气，自动生成数据定时任务
 * @date 2020-07-13 下午4:06:34
 */
@Service("autoDataTask")
public class AutoDataTask implements Job {

    @Autowired
    IotDataServiceI dataService;

    public static void main(String[] args) {

        try {
            JSONObject json = HttpRequest.sendGet("https://restapi.amap.com/v3/weather/weatherInfo", "key=3817b305965202f0bc90d1929ad98e71&city=370200");
            System.out.println(json);
            JSONObject liveJson = json.getJSONArray("lives").getJSONObject(0);

            double a = (Math.random() *(101+1-99)+99)/100f;
            System.out.println(a);

            double temperature = liveJson.getFloat("temperature") * a;
            double humidity = liveJson.getFloat("humidity") * a;
            humidity = humidity>=100?99:humidity;
            System.out.println(String.format("%.2f", temperature));
            System.out.println(String.format("%.2f", humidity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            JSONObject json = HttpRequest.sendGet("https://restapi.amap.com/v3/weather/weatherInfo", "key=3817b305965202f0bc90d1929ad98e71&city=370200");
            System.out.println(json);
            JSONObject liveJson = json.getJSONArray("lives").getJSONObject(0);

            String sql = "select sdd.id as instanceId " + "from jform_scene_device_depoly sdd, jform_base_device bd, jform_scene s "
                    + "where s.is_default_view=1 and s.id=sdd.scene_by and bd.type='terminal' and sdd.device_by=bd.id";
            List<Map<String, Object>> list = dataService.findForJdbc(sql);

            List<IotDataEntity> dataList = new ArrayList<>();
            for (Map<String, Object> map : list) {
                double a = (Math.random() *(101+1-99)+99)/100f;
                double temperature = liveJson.getFloat("temperature") * a;
                double humidity = liveJson.getFloat("humidity") * a;
                humidity = humidity>=100?99:humidity;
                String tempStr = String.format("%.2f", temperature);
                String humidStr = String.format("%.2f", humidity);

                IotDataEntity wendu = new IotDataEntity();
                wendu.setFieldBy("temperature");
                wendu.setLabel("温度");
                wendu.setData(tempStr);
                wendu.setInstanceDeviceBy(map.get("instanceId").toString());
                wendu.setStatus(1);
                wendu.setType("float");
                wendu.setCreateDate(new Date());
                wendu.setDirection(1);

                IotDataEntity shidu = new IotDataEntity();
                shidu.setFieldBy("humidity");
                shidu.setLabel("湿度");
                shidu.setData(humidStr);
                shidu.setInstanceDeviceBy(map.get("instanceId").toString());
                shidu.setStatus(1);
                shidu.setType("float");
                shidu.setCreateDate(new Date());
                shidu.setDirection(1);

//                dataService.save(wendu);
//                dataService.save(shidu);

                dataList.add(wendu);
                dataList.add(shidu);

                String delSql = "delete from jform_iot_data where create_date < date_add(curdate(),INTERVAL -1 month)";
                dataService.executeSql(delSql);
            }

            dataService.batchSave(dataList);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        run();
    }

}
