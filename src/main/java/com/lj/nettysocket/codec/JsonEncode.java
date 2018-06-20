package com.lj.nettysocket.codec;

import com.alibaba.fastjson.JSON;
import com.lj.nettysocket.struct.PMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.msgpack.MessagePack;

/**
 * @Author lj
 * @Date 2018/6/20 10:39
 */
public class JsonEncode extends MessageToByteEncoder<PMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, PMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(Unpooled.copiedBuffer(JSON.toJSONString(msg), CharsetUtil.UTF_8));
    }
}
