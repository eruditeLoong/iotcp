package com.jeecg.nio.mqtt.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jeecgframework.core.util.StringUtil;

public class dataUtil {

	/**
	 * 
	 */
	public static void main(String[] args) {
		// [\[{].+[}\]]
//		String mathReg = "[\\[{].+[}\\]]";
//		System.out.println(getMatcher(mathReg, "898**(*&^%$[{fadf879:78fssd,fdsf:89,[],{}}]"));
//		byte[] bytes = "$".getBytes();
//		System.out.println(Arrays.toString(bytes));
//		System.out.println(bytesTohex(bytes));
//		System.out.println(Arrays.toString(hexTobytes("01034c9778a00000000100020307520000005cac85d80000040034")));
//		
//		System.out.println("接收消息内容(byte->hex->str):" + dataUtil.hexToString("01034c9778a00000000100020307520000005cac85d80000040034247b226964223a20223033222c202254656d7065726174757265223a2233312e34222c2268756d6964697479223a223532227d0a"));
	}

	/**
	 * byte[]转十六进制
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesTohex(byte[] bytes) {
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			boolean flag = false;
			if (b < 0)
				flag = true;
			int absB = Math.abs(b);
			if (flag)
				absB = absB | 0x80;
			// System.out.println(absB & 0xFF);
			String tmp = Integer.toHexString(absB & 0xFF);
			if (tmp.length() == 1) { // 转化的十六进制不足两位，需要补0
				hex.append("0");
			}
			hex.append(tmp.toLowerCase());
		}
		return hex.toString();
	}

	public static String hexToString(String hex) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < hex.length() - 1; i += 2) {
			String s = hex.substring(i, (i + 2));
			int decimal = Integer.parseInt(s, 16);
			sb.append((char) decimal);
			sb2.append(decimal);
		}
		return sb.toString();
	}

	/**
	 * 十六进制转byte[]
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexTobytes(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i = i + 2) {
			String subStr = hex.substring(i, i + 2);
			boolean flag = false;
			int intH = Integer.parseInt(subStr, 16);
			if (intH > 127)
				flag = true;
			if (intH == 128) {
				intH = -128;
			} else if (flag) {
				intH = 0 - (intH & 0x7F);
			}
			byte b = (byte) intH;
			bytes[i / 2] = b;
		}
		return bytes;
	}

	public static String getMatcher(String regex, String source) {
		String result = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(source);
		while (matcher.find()) {
			result = matcher.group(0);
		}
		return result;
	}

	/**
	 * 字符串的分割
	 */
	public static String[] getSplit(String str, String regx) {
		String[] dataStr = str.split(regx);
		return dataStr;
	}

	/**
	 * 字符串的替换
	 */
	public static String getReplace(String str, String regx, String replaceStr) {
		String stri = str.replaceAll(regx, replaceStr);
		return stri;
	}

	/**
	 * 字符串处理之匹配 String类中的match 方法
	 */
	public static Boolean isMatch(String str, String regx) {
		return str.matches(regx);
	}
}
