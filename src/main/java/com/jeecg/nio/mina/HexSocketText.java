package com.jeecg.nio.mina;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.jeecgframework.core.util.HexConvertUtil;

public class HexSocketText {

	public static final String IP_ADDR = "127.0.0.1";// 服务器地址
	public static final int PORT = 8888;// 服务器端口号
	private static Socket socket;

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("客户端启动...");
		socket = new Socket(IP_ADDR, PORT);
		// 2、获取输出流，向服务器端发送信息
		OutputStream os = socket.getOutputStream();// 字节输出流
		PrintWriter pw = new PrintWriter(os);// 将输出流包装成打印流
		while (true) {
			// 23：02 04 00 00 00 02 71 f8
			byte[] b = HexConvertUtil.hexStringToBytes("01 03 04 00 B8 03 3F 3A F6");
			pw.println("01 03 04 00 B8 03 3F 3A F6");
			pw.flush();
			Thread.sleep(30000);
		}
	}
}
