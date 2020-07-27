package com.jeecg.nio.mina;

import org.jeecgframework.core.util.HexConvertUtil;

import jodd.typeconverter.Convert;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 固定格式数据解码类
 * 
 * @author Erudite Loong
 * @date 2019/08/13
 * @version 1.0
 */
public class DecoderUtil {

    /**
     * TH11S温湿度传感器数据解码
     * 
     * @author Erudite Loong
     * @param bytes
     * @return JSONObject
     */
    public static JSONObject TH11SDecoder(byte[] bytes) {
        int len = bytes.length;
        byte addr = bytes[0]; // 设备地址
        byte[] b = new byte[len - 2];
        byte[] c = { bytes[len - 2], bytes[len - 1] };
        System.arraycopy(bytes, 0, b, 0, len - 2);
        // 原报文CRC字符
        String srcCRC = HexConvertUtil.BinaryToHexString(c);
        // 报文内容计算CRC字符
        String destCRC = HexConvertUtil.BinaryToHexString(HexConvertUtil.hexStringToBytes(CRCUtil.getCRC(b)));
        if (srcCRC.equals(destCRC)) {
            int t = Convert.toIntValue(((bytes[3] & 0xff) * 256 + (bytes[4] & 0xff)), 16);
            int h = Convert.toIntValue(((bytes[5] & 0xff) * 256 + ((bytes[6]) & 0xff)), 16);
            t = t <= 32767 ? t : t - 65536;
            h = h <= 32767 ? h : h - 65536;

            JSONObject json = new JSONObject();
            json.put("code", addr);
            json.put("temperature", t / 10.0);
            json.put("humidity", h / 10.0);
            return json;
        } else {
            System.out.println("CRC校验错误！");
            return null;
//            throw new RuntimeException("CRC校验错误！");
        }
    }

    /**
     * WIRELESS温度传感器数据解码
     * 
     * @author Erudite Loong
     * @param bytes
     * @return JSONObject
     */
    public static JSONArray WIRELESSDecoder(byte[] bytes) {
        JSONArray array = new JSONArray();
        int len = bytes.length;
        byte addr = bytes[0]; // 设备地址
        int regNum = 0; // 寄存器数量
        byte[] data; // 数据
        if (len > (127 + 2 + 2)) {
            regNum = Convert.toIntValue((bytes[2] & 0xff) * 256 + (bytes[3] & 0xff), 16);
            data = new byte[regNum];
            System.arraycopy(bytes, 4, data, 0, regNum);
        } else {
            regNum = Convert.toIntValue(bytes[2] & 0xff, 16);
            data = new byte[regNum];
            System.arraycopy(bytes, 3, data, 0, regNum);
        }

        // 原数据
        byte[] b = new byte[len - 2]; // 数据
        System.arraycopy(bytes, 0, b, 0, len - 2);

        // 原报文CRC字符
        byte[] c = { bytes[len - 2], bytes[len - 1] };
        String srcCRC = HexConvertUtil.BinaryToHexString(c);
        // 报文内容计算CRC字符
        String destCRC = HexConvertUtil.BinaryToHexString(HexConvertUtil.hexStringToBytes(CRCUtil.getCRC(b)));
        if (srcCRC.equals(destCRC)) {
            for (int i = 0; i < regNum; i += 2) {
                int t = Convert.toIntValue(((data[i] & 0xff) * 256 + (data[i + 1] & 0xff)), 16);
                if (t == 64537)
                    continue;
                t = t <= 32767 ? t : t - 65536;
//				System.out.println("温度值-"+(i+2)/2+"：" + t);
                JSONObject json = new JSONObject();
                json.put("code", (i + 2) / 2);
                json.put("temperature", t / 10.0);
                array.add(json);
            }
            return array;
        } else {
            System.out.println("CRC校验错误！");
            return null;
        }
    }

    /**
     * WIRELESS120温度传感器数据解码
     * 
     * @author Erudite Loong
     * @param bytes
     * @return JSONObject
     */
    public static JSONObject WIRELESS120Decoder(byte[] bytes) {

        int len = bytes.length;
        byte addr = bytes[0]; // 设备地址
        int regNum = Convert.toIntValue(bytes[2] & 0xff, 16); // 寄存器数量
        // 数据
        byte[] data = new byte[regNum];
        System.arraycopy(bytes, 3, data, 0, regNum);

        // 原报文CRC字符
        byte[] c = { bytes[len - 2], bytes[len - 1] };
        String srcCRC = HexConvertUtil.BinaryToHexString(c);
        // 报文内容计算CRC字符
        byte[] b = new byte[len - 2]; // 数据
        System.arraycopy(bytes, 0, b, 0, len - 2);
        String destCRC = HexConvertUtil.BinaryToHexString(HexConvertUtil.hexStringToBytes(CRCUtil.getCRC(b)));
        if (srcCRC.equals(destCRC)) {
            JSONObject dJson = new JSONObject();
            JSONArray array = new JSONArray();
            for (int i = 0; i < regNum - 4; i += 2) { // regNum-4 舍去最高温2位，舍去报警状态2位// FC19
                int t = Convert.toIntValue(((data[i] & 0xff) * 256 + (data[i + 1] & 0xff)), 16);
                if (t == 64537)
                    continue;
                t = t <= 32767 ? t : t - 65536;
                JSONObject json = new JSONObject();
                json.put("code", (i + 2) / 2);
                json.put("temperature", t / 10.0);
                array.add(json);
            }
            dJson.put("code", addr);
            dJson.put("childData", array);
            return dJson;
        } else {
            System.out.println("CRC校验错误！");
            return null;
        }
    }

}
