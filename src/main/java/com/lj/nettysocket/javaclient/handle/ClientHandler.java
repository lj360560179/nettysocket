package com.lj.nettysocket.javaclient.handle;


import com.lj.nettysocket.javaclient.config.IMClientConfig;
import com.lj.nettysocket.struct.MessageType;
import com.lj.nettysocket.struct.Msg;
import com.lj.nettysocket.struct.PMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        Msg.Builder s = Msg.newBuilder();
        Msg msg = s.setMsgType(TYPE_MSG_AUTH.getValue()).setReceiveId(DEFAULT_RECEIVE_ID).setUid(UID).setMsg(MSG_DEFAULT).build();
        sendMsg(msg);
        LOGGER.info("用户[" + UID + "]成功连接服务器");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Msg m = (Msg) msg;
        LOGGER.info("receive[" + m.getUid() + "]:" + m.getMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOGGER.info("与服务器断开连接：" + cause.getMessage());
    }

    public boolean sendMsg(Msg msg) throws IOException {
        boolean result = msg.getMsg().equals("quit") ? false : true;
        if (result) {
            ctx.writeAndFlush(msg);
        }
        return result;
    }
}
