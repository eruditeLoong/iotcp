package com.jeecg.nio.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.jeecgframework.core.util.HexConvertUtil;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.nio.mqtt.utils.MqttUtil;

import jodd.typeconverter.Convert;

public class TH11SDecoder extends CumulativeProtocolDecoder {

//	@Autowired
//	private MqttSendServer mqtt;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		// 1、先判断数据是否存在
		if (in.remaining() >= 8) {
			// 1、获取长度
			int len = in.remaining();
			// MqttUtil.sendMsg("$forallcn/iotcp/logger", "报文长度：" + len);
			// 复制一个完整消息
			byte[] bytes = new byte[len];
			in.get(bytes);
//			mqtt = ApplicationContextUtil.getContext().getBean(MqttSendServer.class);
//			mqtt.send(this.getClass(), HexConvertUtil.BinaryToHexString(bytes), "$forallcn/iotcp/logger");
			MqttUtil.send(this.getClass(), "$forallcn/iotcp/logger", HexConvertUtil.BinaryToHexString(bytes));
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
				json.put("code", bytes[0]);
				json.put("temperature", t / 10.0);
				json.put("humidity", h / 10.0);
				out.write(json);
				return true;
			} else {
				MqttUtil.send(this.getClass(), "$forallcn/iotcp/logger", "CRC校验错误！");
//				mqtt.send(this.getClass(), "CRC校验错误！", "$forallcn/iotcp/logger");
			}
		} else {
			return false;
		}
		return false;
	}

}
