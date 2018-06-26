package com.lj.nettysocket.server;



import com.lj.nettysocket.proto.Msg;
import com.lj.nettysocket.server.config.IMServerConfig;
import com.lj.nettysocket.server.core.ApplicationContext;
import com.lj.nettysocket.server.handle.ServerHandler;
import com.lj.nettysocket.struct.MessageType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
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
    public Msg getMessage() {
        int toID = -1;
        Msg.Builder s = Msg.newBuilder();
        s.setMsgType(MessageType.TYPE_TEXT.getValue().toString()).setReceiveId(toID).setUid(0).setMsg("");
        return s.build();
    }

    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    public void sendMsg(Msg msg) {
        int receiveID = msg.getReceiveId();
        /**
         * 默认推送所有用户（默认receiveID为-1）
         * */
        if (receiveID == -1) {
            for (Map.Entry<Integer, ChannelHandlerContext> entry : ApplicationContext.onlineUsers.entrySet()) {
                ChannelHandlerContext c = entry.getValue();
                c.writeAndFlush(msg);
            }
            LOGGER.info("推送消息给所有在线用户：" + msg);
        } else {
            ChannelHandlerContext ctx = ApplicationContext.getContext(receiveID);
            if (ctx != null) {
                ctx.writeAndFlush(msg);
                LOGGER.info("推送消息：" + msg);
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

                            // ----Protobuf处理器，这里的配置是关键----
                            ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());// 用于decode前解决半包和粘包问题（利用包头中的包含数组长度来识别半包粘包）
                            //配置Protobuf解码处理器，消息接收到了就会自动解码，ProtobufDecoder是netty自带的，Message是自己定义的Protobuf类
                            ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(Msg.getDefaultInstance()));
                            // 用于在序列化的字节数组前加上一个简单的包头，只包含序列化的字节长度。
                            ch.pipeline().addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                            //配置Protobuf编码器，发送的消息会先经过编码
                            ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
                            // ----Protobuf处理器END----
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
