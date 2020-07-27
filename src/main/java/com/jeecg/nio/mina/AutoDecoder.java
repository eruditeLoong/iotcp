package com.jeecg.nio.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.jeecgframework.core.util.HexConvertUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoDecoder extends CumulativeProtocolDecoder {
	private static final Logger logger = LoggerFactory.getLogger(MinaDataDealThread.class);

	@Autowired
	private SystemService systemService;

	/**
	 *
	 * @param session
	 * @param in
	 * @param out
	 * @return
	 * @throws Exception
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		System.out.println(in.toString());
		int len = in.remaining();
		if (len > 1) {
			byte[] bytes = new byte[len];
			in.get(bytes);
			String hexString = HexConvertUtil.BinaryToHexString(bytes);
			out.write(bytes);
			return true;
		}

		return false;
	}
}
