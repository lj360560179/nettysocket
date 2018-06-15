package com.lj.nettysocket.server;


import com.lj.nettysocket.codec.MsgPackDecode;
import com.lj.nettysocket.codec.MsgPackEncode;
import com.lj.nettysocket.server.config.IMServerConfig;
import com.lj.nettysocket.server.core.ApplicationContext;
import com.lj.nettysocket.server.handle.ServerHandler;
import com.lj.nettysocket.struct.IMMessage;
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

import java.io.IOException;
import java.util.Map;

/**
 * @Author lj
 * @Date 2018/6/13 14:21
 */
public class IMServer implements Runnable, IMServerConfig {

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
    public IMMessage getMessage() {
        int toID = -1;
        IMMessage message = new IMMessage(SERVER_ID, TYPE_MSG_TEXT.getValue(), toID, MSG_DEFAULT);
        return message;
    }

    /**
     * 发送消息
     *
     * @param msg
     * @return
     */
    public boolean sendMsg(IMMessage msg) {
        // 当用户输入quit表示退出，不在进行推送
        boolean result = msg.getMsg().equals("quit") ? false : true;
        if (result) {
            int receiveID = msg.getReceiveId();
            String content = msg.getMsg();
            if (content.startsWith("#") && content.indexOf(":") != -1) {
                try {
                    /**
                     * 用户输入指定的推送客户端
                     * 输入文本格式为： ＂＃8888:发送内容＂
                     * “#”和“：”之间内容为用户ID，“：”之后为推送消息内容
                     */
                    receiveID = Integer.valueOf(content.substring(1, content.indexOf(":")));
                    msg.setReceiveId(receiveID);
                    msg.setMsg(content.substring(content.indexOf(":")));
                } catch (NumberFormatException e) {
                    //解析失败则，默认发送所有
                    e.printStackTrace();
                }
            }

            /**
             * 默认推送所有用户（默认receiveID为-1）
             * */
            if (receiveID == -1) {
                System.out.println("推送消息给所有在线用户：" + msg);
                for (Map.Entry<Integer, ChannelHandlerContext> entry : ApplicationContext.onlineUsers.entrySet()) {
                    ChannelHandlerContext c = entry.getValue();
                    c.writeAndFlush(msg);
                }
            } else {
                ChannelHandlerContext ctx = ApplicationContext.getContext(receiveID);
                if (ctx != null) {
                    System.out.println("推送消息：" + msg);
                    ctx.writeAndFlush(msg);
                }
            }
        }
        return result;
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
                            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                            ch.pipeline().addLast("msgpack decoder", new MsgPackDecode());
                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            ch.pipeline().addLast("msgpack encoder", new MsgPackEncode());
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
