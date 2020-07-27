package com.jeecg.nio.mina;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientKeepAliveThread extends Thread {
	private Socket socket;

	public ClientKeepAliveThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(inputStreamReader);
			OutputStream os = socket.getOutputStream();// 字节输出流
			PrintWriter pw = new PrintWriter(os);// 将输出流包装成打印流
			try {
				// 信息的格式：(login||logout||say),发送人,收发人,信息体
				while (true) {
					pw.println("1");
					pw.flush();
					Thread.sleep(10000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
