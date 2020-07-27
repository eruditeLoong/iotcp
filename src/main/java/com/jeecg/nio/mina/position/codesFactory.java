package com.jeecg.nio.mina.position;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

import java.nio.charset.Charset;

/**
 * @name:
 * @description:
 * @author: Erudite Loong
 * @version: 1.0
 * @time: 2019/9/2 0002 12:07
 */
public class codesFactory implements ProtocolCodecFactory {

    final static char endchar = 0x0d;
    private final TextLineEncoder encoder;
    private final ProtocolDecoder decoder;

    public codesFactory() {
        this(Charset.forName("utf-8"));
    }

    public codesFactory(Charset charset) {
        encoder = new TextLineEncoder(charset, LineDelimiter.UNIX);
        decoder = new GPSCloudDecoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }
}
