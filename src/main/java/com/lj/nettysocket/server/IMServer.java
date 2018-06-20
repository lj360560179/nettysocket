package com.lj.nettysocket.server;


import com.lj.nettysocket.client.handle.ClientHandler;
import com.lj.nettysocket.codec.MsgPackDecode;
import com.lj.nettysocket.codec.MsgPackEncode;
import com.lj.nettysocket.server.config.IMServerConfig;
import com.lj.nettysocket.server.core.ApplicationContext;
import com.lj.nettysocket.server.handle.ServerHandler;
import com.lj.nettysocket.struct.PMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @Author lj
 * @Date 2018/6/13 14:21
 */
public class IMServer implements Runnable, IMServerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(IMServer.class);

    /**
     * 启动服务
     *
     * @throws IOException
     */
    public void start() throws IOException {
        new Thread(this).start();
    }

    /**
     * 获取推送消息
     *
     * @return
     */
    public PMessage getMessage() {
        int toID = -1;
        PMessage message = new PMessage(SERVER_ID, TYPE_MSG_TEXT.getValue(), toID, MSG_DEFAULT);
        return message;
    }

    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    public void sendMsg(PMessage msg) {
        int receiveID = msg.getReceiveId();
        String content = msg.getMsg();
        /**
         * 默认推送所有用户（默认receiveID为-1）
         * */
        if (receiveID == -1) {
            LOGGER.info("推送消息给所有在线用户：" + msg);
            for (Map.Entry<Integer, ChannelHandlerContext> entry : ApplicationContext.onlineUsers.entrySet()) {
                ChannelHandlerContext c = entry.getValue();
                c.writeAndFlush(msg);
            }
        } else {
            ChannelHandlerContext ctx = ApplicationContext.getContext(receiveID);
            if (ctx != null) {
                LOGGER.info("推送消息：" + msg);
                ctx.writeAndFlush(msg);
            }
        }
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
//                            ch.pipeline().addLast("msgpack decoder", new MsgPackDecode());
//                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
//                            ch.pipeline().addLast("msgpack encoder", new MsgPackEncode());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(SERVER_PORT).sync();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
