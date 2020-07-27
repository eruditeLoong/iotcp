package com.jeecg.nio.mina;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jeecg.nio.mqtt.service.MqttPublishClient;

@Component
public class ServerHandler extends IoHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

//	@Autowired
//	private MqttSendSevice mqttSendSevice;

    private MqttPublishClient mqttPublish;

    public static ConcurrentHashMap<String, IoSession> sessionsConcurrentHashMap = new ConcurrentHashMap<String, IoSession>();

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        session.closeOnFlush();
        logger.error("会话发生异常，请关闭它：{}", cause.getMessage());
        this.mqttPublish.publishMessage(this.getClass(), "error", "[mqtt]会话发生异常，请关闭它：" + cause.getMessage(), "$forallcn/iotcp/logger");

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String addr = session.getRemoteAddress().toString();
//		logger.info("服务器收到[{}}]的消息：{}", addr, message);
        this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]服务器收到[" + addr + "]的消息：" + message, "$forallcn/iotcp/logger");
        MinaDataDealThread dataDeal = new MinaDataDealThread(addr, message);
        Thread thread = new Thread(dataDeal);
        thread.start();
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
//		logger.info("给客户端：[" + session.getRemoteAddress() + "] 发送消息：" + message);
        this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]给客户端：[" + session.getRemoteAddress() + "] 发送消息：" + message, "$forallcn/iotcp/logger");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        String addr = session.getRemoteAddress().toString();
        logger.info("$forallcn/iotcp/logger", "客户端：[" + addr + "] 连接成功！");
        try {
            this.mqttPublish = MqttPublishClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]客户端：[" + addr + "] 连接成功！", "$forallcn/iotcp/logger");
        // 启动主动请求数据线程，在线程启动前，判断设备是否是主动请求数据，不是的话自动关闭线程
        RequestDataThread dataDeal = new RequestDataThread(session);
        Thread thread = new Thread(dataDeal);
        thread.start();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        logger.error("sessionClosed: {}", session.getRemoteAddress().toString());
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.error("sessionIdle: {}", session.getRemoteAddress().toString());
        super.sessionIdle(session, status);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.error("sessionOpened: {}", session.getRemoteAddress().toString());
        this.mqttPublish.publishMessage(this.getClass(), "info", "[mqtt]连接开启", "$forallcn/iotcp/logger");

    }
}
