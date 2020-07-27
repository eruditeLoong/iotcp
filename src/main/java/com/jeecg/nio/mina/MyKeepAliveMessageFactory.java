package com.jeecg.nio.mina;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.keepalive.KeepAliveMessageFactory;

public class MyKeepAliveMessageFactory implements KeepAliveMessageFactory {

	private final Logger LOG = Logger.getLogger(MyKeepAliveMessageFactory.class);

	/** 心跳包内容 */
	private static final String HEARTBEATREQUEST = "0"; // 发送
	private static final String HEARTBEATRESPONSE = "1"; // 接收

	@Override
	public boolean isRequest(IoSession session, Object message) {
		if (message.equals(HEARTBEATREQUEST)) {
			System.out.println("收到心跳包：" + message);
			return true;
		}
		return false;
	}

	@Override
	public boolean isResponse(IoSession session, Object message) {
		if (message.equals(HEARTBEATRESPONSE))
			return true;
		return false;
	}

	@Override
	public Object getRequest(IoSession session) {
		return HEARTBEATREQUEST;
	}

	@Override
	public Object getResponse(IoSession session, Object request) {
		/** 返回预设语句 */
		return HEARTBEATRESPONSE;
	}

}
