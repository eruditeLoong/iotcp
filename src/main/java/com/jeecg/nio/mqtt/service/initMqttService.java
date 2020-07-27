package com.jeecg.nio.mqtt.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.jeecg.nio.mqtt.utils.MqttUtil;

//@Component
public class initMqttService implements InitializingBean{

	@Override
	public void afterPropertiesSet() throws Exception {
		MqttUtil.getMqttClient();
	}

}
