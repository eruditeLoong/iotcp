package com.jeecg.nio.mina.position;

import com.jeecg.nio.mqtt.service.MqttPublishClient;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.jeecgframework.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class GPSServerHandler extends IoHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GPSServerHandler.class);
    public static ConcurrentHashMap<String, IoSession> sessionsConcurrentHashMap = new ConcurrentHashMap<String, IoSession>();

    private MqttPublishClient mqttPublish;

//    @Autowired
//    private MqttSendSevice mqttSendSevice;

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        if (session != null) {
            String addr = session.getAttribute("id").toString();
            if (StringUtil.isNotBlank(addr)) {
                sessionsConcurrentHashMap.remove(addr);
            }
            logger.info("{}会话发生异常,连接总数：{}", addr, sessionsConcurrentHashMap.size() + "");
            this.mqttPublish.publishMessage("$forallcn/iotcp/position/sessionSize", sessionsConcurrentHashMap.size() + "", 0);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String addr = session.getRemoteAddress().toString();
        GPSDealThread dataDeal = new GPSDealThread(session, message);
        Thread t = new Thread(dataDeal);
        t.start();
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        this.mqttPublish.publishMessage(GPSServerHandler.class, "INFO", session.getAttribute("imei") +"send message：" + message, "$forallcn/iotcp/logger");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        this.mqttPublish = MqttPublishClient.getInstance();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        String addr = session.getRemoteAddress().toString();
        sessionsConcurrentHashMap.remove(addr.split(":")[0]);
        this.mqttPublish.publishMessage("$forallcn/iotcp/position/sessionSize", sessionsConcurrentHashMap.size() + "", 0);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        super.sessionIdle(session, status);
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        String addr = session.getRemoteAddress().toString();
        session.setAttribute("id", addr.split(":")[0]);
        sessionsConcurrentHashMap.put(addr.split(":")[0], session);
        logger.info("{}连接成功,连接总数：{}", addr, session.getService().getManagedSessions().size() + "/" + sessionsConcurrentHashMap.size());
        this.mqttPublish.publishMessage("$forallcn/iotcp/position/sessionSize", sessionsConcurrentHashMap.size() + "", 0);
    }
}
