package com.jeecg.nio.mina.position;

import com.alibaba.fastjson.JSONObject;
import jodd.typeconverter.Convert;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.jeecgframework.core.util.HexConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @name:
 * @description:
 * @author: Erudite Loong
 * @version: 1.0
 * @time: 2019/9/2 0002 12:14
 */
public class GPSCloudDecoder extends CumulativeProtocolDecoder {
    private static final Logger logger = LoggerFactory.getLogger(GPSCloudDecoder.class);

    /**
     * @param session The current Session
     * @param in      the cumulative buffer
     * @param out     The {@link ProtocolDecoderOutput} that will receive the decoded message
     * @return <tt>true</tt> if and only if there's more to decode in the buffer
     * @throws Exception if cannot decode <tt>in</tt>.
     */
    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        // 数据总长度
        int size = in.remaining();
        if (size > 2) {//前2字节是包头
            byte[] data = new byte[size];
            in.get(data);
            if (in.remaining() > 0) {//如果读取一个完整包内容后还粘了包，就让父类再调用一次，进行下一次解析
                return false;
            }
            // 判断包头是否是0x78,0x78
            if (data[0] != 0x78 || data[1] != 0x78) {
                return false;
            }
            // 数据长度 1byte， 数据长度的值为数据内容所占字节数与协议号所占字节数的总和
            int len = Convert.toIntValue((data[2] & 0xff), 16);
            // 协议号 1byte
            int num = data[3] & 0xff;
            // 数据
            byte[] d = new byte[len];
            System.arraycopy(data, 3, d, 0, len);
            // 软件版本号
            int version = Convert.toIntValue((data[size - 3] & 0xff), 16);
            // 停止位 2byte
            // 判断停止位是否是0x0d,0x0a
            if (data[size - 2] != 0x0d || data[size - 1] != 0x0a) {
//                in.reset();
                return false;
            }
            JSONObject json = new JSONObject();
            json.put("msgLen", size);
            json.put("msg", HexConvertUtil.BinaryToHexString(data));
            json.put("dataLen", len);
            json.put("data", HexConvertUtil.BinaryToHexString(d));
            json.put("num", num);
            out.write(json);
            return true;
        }
        //处理成功，让父类进行接收下个包
        return false;
    }
}
