package com.jeecg.nio.mqtt.service;

import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MqttSendSevice {

	@Resource
	private MqttPahoMessageHandler mqtt;

	/**
	 * 默认订阅主题
	 */
	private final String TOPIC = "$forallcn/iotcp";

	/**
	 * 消息发送
	 * @param payload 消息主体
	 */
	public void send(String payload) {
		Message<String> message = MessageBuilder.withPayload(payload).setHeader("mqtt_topic", TOPIC).build();
		mqtt.handleMessage(message);
	}

	/**
	 * 消息发送
	 * @param payload 消息主体
	 * @param topic 订阅主题
	 */
	public void send(String payload, String topic) {
		Message<String> message = MessageBuilder.withPayload(payload).setHeader("mqtt_topic", topic).build();
		mqtt.handleMessage(message);
	}

	/**
	 * 调试消息发送
	 * @param clazz 调用class
	 * @param payload 消息主体
	 * @param topic 订阅主题
	 */
	public void send(Class clazz, String payload, String topic) {
		Message<String> message = MessageBuilder.withPayload("[" + clazz.getName() + "] " + payload).setHeader("mqtt_topic", topic).build();
		mqtt.handleMessage(message);
	}

	/**
	 * 调试消息发送
	 * @param clazz 调用class
	 * @param type 消息类型：info、debug、warn、error
	 * @param payload 消息主体
	 * @param topic 订阅主题
	 */
	public void send(Class clazz, String type, String payload, String topic) {
		Message<String> message = MessageBuilder.withPayload("[" + clazz.getName() + "] [" + type + "]" + payload).setHeader("mqtt_topic", topic).build();
		mqtt.handleMessage(message);
	}
}
