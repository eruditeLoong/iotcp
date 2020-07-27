package com.jeecg.nio.mina;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

public class MyCodesFactory implements ProtocolCodecFactory {

	private final TextLineEncoder encoder;
	private final ProtocolDecoder decoder;
	final static char endchar = 0x0d;

	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	public MyCodesFactory() {
		this(Charset.forName("utf-8"));
	}

	public MyCodesFactory(Charset charset) {
		encoder = new TextLineEncoder(charset, LineDelimiter.UNIX);
//		decoder = new TH11SDecoder();
		decoder = new AutoDecoder();
	}
}
