package com.lj.nettysocket.codec;

import com.lj.nettysocket.struct.IMMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @Author lj
 * @Date 2018/6/13 14:24
 */
public class MsgPackEncode extends MessageToByteEncoder<IMMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IMMessage msg, ByteBuf out) throws Exception {
        out.writeBytes(new MessagePack().write(msg));
    }
}
