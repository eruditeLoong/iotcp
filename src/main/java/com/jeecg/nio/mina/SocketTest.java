package com.jeecg.nio.mina;

import org.jeecgframework.core.util.HexConvertUtil;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class SocketTest extends Thread {

	public static final String IP_ADDR = "192.168.1.2";// 服务器地址
	public static final int PORT = 8889;// 服务器端口号
	private static Socket socket;

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("客户端启动...");
		socket = new Socket(IP_ADDR, PORT);
		if (socket != null) {
			System.out.println("客户端启动成功：" + socket.toString());
		}
		// 开启一个线程接收信息，并解析
		ClientKeepAliveThread thread = new ClientKeepAliveThread(socket);
		thread.setName("ClientKeepAliveThread");
//		thread.start();
		SocketTest cmd = new SocketTest();
		cmd.start();
		// 2、获取输出流，向服务器端发送信息
//		OutputStream os = socket.getOutputStream();// 字节输出流
//		while (true) {
//			// 23：02 04 00 00 00 02 71 f8
//			System.out.println("**********");
//			// 31 0D 0A 02 04 00 00 00 02 71 F8
//			 String data  = "01 03 48 01 2D 01 2C 01 22 01 22 01 23 01 25 01 23 01 23 01 22 01 23 01 23 01 23 01 22 01 23 01 22 01 22 01 22 01 24 01 23 01 23 01 23 01 24 01 24 01 22 01 22 01 21 01 21 01 23 01 22 01 22 01 25 01 23 01 24 01 23 01 23 01 22 DC DA";
//			 String data1 = "01 03 48 01 2D 01 2C 06 22 01 22 01 23 01 25 01 23 01 23 01 22 01 23 01 23 01 23 01 22 01 23 01 22 01 22 01 22 01 24 01 23 01 23 01 23 01 24 01 24 01 22 01 22 01 21 01 21 01 23 01 22 01 22 01 25 01 23 01 24 01 23 01 23 01 22 69 37";
//			byte[] b = HexConvertUtil.hexStringToBytes(data1);
//			os.write(b);
//			Thread.sleep(60000);
//		}
	}

	public void run() {
		try {
			System.out.println("进入接收线程");
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			OutputStream os = socket.getOutputStream();// 字节输出流
			PrintWriter pw = new PrintWriter(os);// 将输出流包装成打印流
			while (socket.isConnected()) {
				byte[] b = new byte[10];
				int rc=0;
				int c = 0;
				while( (rc = is.read(b, c, 8) )>0){
					c = is.read(b, 0, rc);
					System.out.println("接收服务器的信息：" + Arrays.toString(b));
					String data1 = "01 03 48 01 2D 01 2C 06 22 01 22 01 23 01 25 01 23 01 23 01 22 01 23 01 23 01 23 01 22 01 23 01 22 01 22 01 22 01 24 01 23 01 23 01 23 01 24 01 24 01 22 01 22 01 21 01 21 01 23 01 22 01 22 01 25 01 23 01 24 01 23 01 23 01 22 69 37";
					os.write(HexConvertUtil.hexStringToBytes(data1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
