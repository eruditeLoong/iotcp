package com.jeecg.nio.mqtt.service;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPublishClient {
    private static final Logger logger = LoggerFactory.getLogger(MqttPublishClient.class);

    public static org.eclipse.paho.client.mqttv3.MqttClient mqttClient = null;
    private static MemoryPersistence memoryPersistence = null;
    private static MqttConnectOptions mqttConnectOptions = null;

    private static MqttPublishClient instance = null;

    public static MqttPublishClient getInstance() throws Exception {
        if (instance == null) {
            synchronized (MqttPublishClient.class) {
                if (instance == null) {
                    instance = new MqttPublishClient();
                }
            }
        }
        return instance;
    }

    public MqttPublishClient() {
        init("forallcn-iotcp-publish-client-" + new Date().getTime());
    }

    public void init(String clientId) {
        // 初始化连接设置对象
        mqttConnectOptions = new MqttConnectOptions();
        // 初始化MqttClient
        if (null != mqttConnectOptions) {
//			true可以安全地使用内存持久性作为客户端断开连接时清除的所有状态
            mqttConnectOptions.setCleanSession(true);
//			设置连接超时
            mqttConnectOptions.setConnectionTimeout(30);
            mqttConnectOptions.setMaxInflight(80000);
//			设置持久化方式
            memoryPersistence = new MemoryPersistence();
            if (null != memoryPersistence && null != clientId) {
                try {
                    mqttClient = new org.eclipse.paho.client.mqttv3.MqttClient("tcp://115.28.230.81:1883", clientId, memoryPersistence);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {

            }
        } else {
            logger.error("mqttConnectOptions对象为空");
        }
        // 设置连接和回调
        if (null != mqttClient) {
            if (!mqttClient.isConnected()) {
                try {
                    logger.info("创建连接:" + mqttClient.isConnected());
                    mqttClient.connect(mqttConnectOptions);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }
        } else {
            logger.error("mqttClient为空");
        }
    }

    // 关闭连接
    public void closeConnect() {
        // 关闭存储方式
        if (null != memoryPersistence) {
            try {
                memoryPersistence.close();
            } catch (MqttPersistenceException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("memoryPersistence is null");
        }

//		关闭连接
        if (null != mqttClient) {
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                    mqttClient.close();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("mqttClient is not connect");
            }
        } else {
            logger.error("mqttClient is null");
        }
    }

    // 发布消息
    public void publishMessage(String pubTopic, String message, int qos) {
        if (null != mqttClient && mqttClient.isConnected()) {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setQos(qos);
            mqttMessage.setPayload(message.getBytes());
            MqttTopic topic = mqttClient.getTopic(pubTopic);
            if (null != topic) {
                try {
                    MqttDeliveryToken publish = topic.publish(mqttMessage);
                    if (!publish.isComplete()) {
                        // logger.info("消息发布成功");
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        } else {
            reConnect();
        }
    }

    /**
     * 发送日志消息
     *
     * @param clazz
     * @param type
     * @param payload
     * @param topic
     */
    public void publishMessage(Class clazz, String type, String payload, String topic) {
        String message = "[" + clazz.getName() + "] [" + type + "]" + payload;
        this.publishMessage(topic, message, 0);
    }

    // 重新连接
    public void reConnect() {
        if (null != mqttClient) {
            if (!mqttClient.isConnected()) {
                if (null != mqttConnectOptions) {
                    try {
                        mqttClient.connect(mqttConnectOptions);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.error("mqttConnectOptions is null");
                }
            } else {
                logger.error("mqttClient is null or connect");
            }
        } else {
            init("admin");
        }

    }

    // 订阅主题
    public void subTopic(String topic) {
        if (null != mqttClient && mqttClient.isConnected()) {
            try {
                mqttClient.subscribe(topic, 1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("mqttClient is error");
        }
    }

    // 清空主题
    public void cleanTopic(String topic) {
        if (null != mqttClient && !mqttClient.isConnected()) {
            try {
                mqttClient.unsubscribe(topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            logger.error("mqttClient is error");
        }
    }

    public static void main(String[] args) {
        MqttPublishClient mqttClient = new MqttPublishClient();
        mqttClient.publishMessage("marketAll", "12312312312", 1);
    }
}