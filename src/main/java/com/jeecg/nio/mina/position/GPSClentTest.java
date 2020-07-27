package com.jeecg.nio.mina.position;

import org.jeecgframework.core.util.HexConvertUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class GPSClentTest  extends Thread{
    public static final String IP_ADDR = "192.168.1.101";// 服务器地址
    public static final int PORT = 18889;// 服务器端口号
    private static Socket socket;
    public static void main(String[] args) throws IOException, InterruptedException {
        GPSClentTest gps = new GPSClentTest();
        for(int i=0;i<400;i++) {
            gps.createConnet(i);
            Thread.sleep(200);
        }
    }

    public void createConnet(int i) {
        try {
            socket = new Socket(IP_ADDR, PORT);
            if (socket != null) {
                System.out.println("客户端[" + i + "]启动成功：" + socket.toString());
            }
            OutputStream os = socket.getOutputStream();// 字节输出流
            byte[] bNum = {(byte) i};
            System.out.println(bNum[0]);
            String data = "78 78 0A 01 01 23 45 67 89 01 23 45 " + HexConvertUtil.BinaryToHexString(bNum) + " 0D 0A";
            os.write(HexConvertUtil.hexStringToBytes(data));
            GPSClentTest t = new GPSClentTest();
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                OutputStream os = socket.getOutputStream();// 字节输出流
                String data1 = "78 78 01 08 0D 0A";
                os.write(HexConvertUtil.hexStringToBytes(data1));
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
