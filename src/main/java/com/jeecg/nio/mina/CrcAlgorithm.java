package com.jeecg.nio.mina;

import org.jeecgframework.core.util.HexConvertUtil;

import com.alibaba.fastjson.JSONObject;

import jodd.typeconverter.Convert;

public class CrcAlgorithm {

	public static void main(String[] args) {
		String cmd = "03 04 04 01 09 01 7c 09 CB"; // 71 f8
		byte[] b = HexConvertUtil.hexStringToBytes(cmd);
		int len = b.length;
		byte[] bb = new byte[len - 2];
		byte[] b2 = new byte[2];
		System.arraycopy(b, 0, bb, 0, len - 2);
		System.arraycopy(b, len - 2, b2, 0, 2);
		System.out.println(HexConvertUtil.BinaryToHexString(b2));
		System.out.println(HexConvertUtil.BinaryToHexString(bb));
		System.out.println(HexConvertUtil.BinaryToHexString(HexConvertUtil.hexStringToBytes(CRCUtil.getCRC(bb))));

		System.out.println("--------------------------------------");
		byte[] data = { 0x02, 0x04, 0x04, (byte) 0xff, (byte) 0x9b, 0x01, (byte) 0xA1, (byte) 0x98, (byte) 0x93 };
		System.out.println(HexConvertUtil.BinaryToHexString(data));

		int t = Convert.toIntValue(((data[3] & 0xff) * 256 + (data[4] & 0xff)), 16);
		int h = Convert.toIntValue(((data[5] & 0xff) * 256 + ((data[6]) & 0xff)), 16);
		t = t <= 32767 ? t : t - 65536;
		h = h <= 32767 ? h : h - 65536;
		JSONObject json = new JSONObject();
		json.put("code", data[0]);
		json.put("temperature", t / 10.0);
		json.put("humidity", h / 10.0);
		System.out.println(json.toJSONString());
	}
}
