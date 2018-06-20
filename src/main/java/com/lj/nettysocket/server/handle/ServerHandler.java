package com.lj.nettysocket.server.handle;


import com.lj.nettysocket.server.core.ApplicationContext;
import com.lj.nettysocket.struct.MessageType;
import com.lj.nettysocket.struct.PMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import com.alibaba.fastjson.JSON;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @Author lj
 * @Date 2018/6/13 14:11
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("服务端Handler创建。。。");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        super.channelActive(ctx);
        LOGGER.info("有客户端连接：" + ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        PMessage message = JSON.parseObject(in.toString(CharsetUtil.UTF_8), PMessage.class);
        LOGGER.info(message.toString());
        if (message != null) {
            if (message.getMsgType().equals(MessageType.TYPE_AUTH.getValue())) {
                ApplicationContext.add(message.getUid(), ctx);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.info("与客户端断开连接:" + cause.getMessage());
        ctx.close();
    }
}