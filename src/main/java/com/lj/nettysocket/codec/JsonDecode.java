package com.lj.nettysocket.codec;

import com.alibaba.fastjson.JSON;
import com.lj.nettysocket.struct.PMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;


import java.util.List;

/**
 * @Author lj
 * @Date 2018/6/20 10:47
 */
public class JsonDecode extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final int length = msg.readableBytes();
        final  byte[] array = new byte[length];
        msg.getBytes(msg.readerIndex(),array,0,length);
        out.add(JSON.parseObject(msg.toString(CharsetUtil.UTF_8), PMessage.class));
    }
}
