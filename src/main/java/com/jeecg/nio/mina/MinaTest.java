package com.jeecg.nio.mina;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MinaTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("spring-mvc-mina.xml");

		// 创建客户端连接器.
//		NioSocketConnector connector = new NioSocketConnector();
//		connector.setDefaultLocalAddress(new InetSocketAddress(1009));
//		connector.getFilterChain().addLast("logger", new LoggingFilter());
//		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8")))); // 设置编码过滤器
//		connector.setHandler(new ClientHandler());// 设置事件处理器
//		ConnectFuture cf = connector.connect(new InetSocketAddress("127.0.0.1", 8888));// 建立连接
//		cf.awaitUninterruptibly();// 等待连接创建完成
//		cf.getSession().write("hello,测试！");// 发送消息，中英文都有
		//cf.getSession().closeOnFlush();
		// cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开
		// connector.dispose();
	}

}