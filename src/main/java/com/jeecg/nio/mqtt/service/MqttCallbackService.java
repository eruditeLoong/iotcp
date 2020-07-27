package com.jeecg.nio.mqtt.service;

import java.util.Arrays;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeecg.nio.mqtt.utils.MqttUtil;
import com.jeecg.nio.mqtt.utils.dataUtil;

public class MqttCallbackService implements MqttCallback {
	private static final Logger logger = LoggerFactory.getLogger(MqttCallbackService.class);

	@Autowired
	private SystemService systemService;

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("mqtt断开");
		while (true) {
			try {// 如果没有发生异常说明连接成功，如果发生异常，则死循环
				MqttUtil.getMqttClient();
				Thread.sleep(5000);
				System.out.println("重新连接。。");
				break;
			} catch (Exception e) {
				continue;
			}
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// publish后会执行到这里
		System.out.println("$forallcn/iotcp/logger");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("MQTT收到消息："+message);
		// subscribe后得到的消息会执行到这里面
		if(topic.indexOf("/datas")!=-1) {
//			messageDeal(topic, message);
//			MqttDataDealThread dataDeal = new MqttDataDealThread(topic, message);
//			Thread thread = new Thread(dataDeal);
//			thread.start();
		}
	}
}