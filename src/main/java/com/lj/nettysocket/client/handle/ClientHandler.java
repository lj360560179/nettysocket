package com.lj.nettysocket.client.handle;

import com.alibaba.fastjson.JSON;
import com.lj.nettysocket.client.config.IMClientConfig;
import com.lj.nettysocket.server.handle.ServerHandler;
import com.lj.nettysocket.struct.PMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * @Author lj
 * @Date 2018/6/13 14:21
 */
public class ClientHandler extends ChannelInboundHandlerAdapter implements IMClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        //通道建立时发送认证消息给服务器
        PMessage message = new PMessage(UID, TYPE_MSG_AUTH.getValue(), DEFAULT_RECEIVE_ID, MSG_DEFAULT);
        sendMsg(message);
        LOGGER.info("用户[" + UID + "]成功连接服务器");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        PMessage message = JSON.parseObject(in.toString(CharsetUtil.UTF_8), PMessage.class);
        LOGGER.info("receive[" + message.getUid() + "]:" + message.getMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOGGER.info("与服务器断开连接：" + cause.getMessage());
    }

    public boolean sendMsg(PMessage msg) throws IOException {
        boolean result = msg.getMsg().equals("quit") ? false : true;
        if (result) {
            String m = JSON.toJSONString(msg);
            ctx.writeAndFlush(Unpooled.copiedBuffer(m, CharsetUtil.UTF_8));
        }
        return result;
    }
}
