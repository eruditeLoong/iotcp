package com.jeecg.data.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jeecg.nio.mqtt.utils.MqttUtil;

@Controller
@RequestMapping("/mqtt")
public class MqttController {

//	@Resource
//	private MqttPahoMessageHandler mqtt;
	
	@RequestMapping(value="/send")
	public void sendMessage(String payload, String topic, String clientId){
//		Message<String> message = MessageBuilder.withPayload(payload).setHeader(topic, clientId).build();
//		mqtt.handleMessage(message);
		MqttUtil.send(this.getClass(), topic, payload);
	}
}
