package com.jeecg.nio.mqtt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecg.nio.mqtt.service.MqttCallbackService;

/**
 */
public class MqttUtil {
	private static final Logger logger = LoggerFactory.getLogger(MqttUtil.class);

	private static Properties properties = new Properties();
	static {
		InputStream ips = MqttUtil.class.getClassLoader().getResourceAsStream("mqtt.properties");
		try {
			properties.load(ips);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static MqttClient client = null;

	public static final String serverURI = properties.getProperty("mqtt.serverURI");
	public static final String clientId = properties.getProperty("mqtt.clientId");
	public static final String defaultTopic = properties.getProperty("mqtt.defaultTopic");
	public static final int defaultQos = Integer.valueOf(properties.getProperty("mqtt.defaultQos"));
	public static final String username = properties.getProperty("mqtt.username");
	public static final String password = properties.getProperty("mqtt.password");

	private static MqttClient connect() throws MqttException {
		MemoryPersistence persistence = new MemoryPersistence();
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(false);
		connOpts.setUserName(username);
		connOpts.setPassword(password.toCharArray());
		connOpts.setConnectionTimeout(10);
		connOpts.setKeepAliveInterval(60);
//		String[] uris = {"tcp://10.100.124.206:1883","tcp://10.100.124.207:1883"};
//		connOpts.setServerURIs(uris);  //起到负载均衡和高可用的作用
		MqttClient mqttClient = new MqttClient(serverURI, clientId, persistence);
		mqttClient.setCallback(new MqttCallbackService());
		mqttClient.connect(connOpts);
		mqttClient.subscribe(defaultTopic, defaultQos);
		logger.info("  ------ 初始化mqtt客户端，并订阅主题 【{}】------", defaultTopic);
		return mqttClient;
	}

	public static MqttClient getMqttClient() {
		// 一当前类的这个线程作为key去取
		// 如果Threadloader类中没有Connection连接，就调用getConn创建一个连接
		if (client == null) {
			try {
				// 调用getConn创建 并 返回一个连接
				client = connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 将创建的连接放入到Threadloader中，用于其他程序调用
			// connections.set(client);
		}
		return client;
	}

	public static void send(String msg) {
		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(defaultQos);
		message.setRetained(false);
		try {
			// 发送消息
			client.publish(defaultTopic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void send(Class clazz, String topic, String msg) {
		MqttMessage message = new MqttMessage(("[" + clazz.getName() + "] " + msg).getBytes());
		message.setQos(defaultQos);
		message.setRetained(false);
		try {
			// 发送消息
			client.publish(topic, message);
			// client.setManualAcks(true);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void send(String topic, String msg) {
		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(defaultQos);
		message.setRetained(false);
		try {
			// 发送消息
			client.publish(topic, message);
			// client.setManualAcks(true);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void send(String topic, String msg, int qos) {
		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(qos);
		message.setRetained(false);
		try {
			// 发送消息
			client.publish(topic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void subscribe() {
		try {
			client.subscribe(defaultTopic, defaultQos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void subscribe(String topic, int qos) {
		try {
			client.subscribe(topic, qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public static void closeClient() {
		if (client != null && client.isConnected()) {
			try {
				// 断开连接
				client.disconnect();
				// 关闭客户端
				client.close();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		MqttUtil.getMqttClient();
	}
}
