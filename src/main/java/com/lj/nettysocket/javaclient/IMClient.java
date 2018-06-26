package com.lj.nettysocket.javaclient;

import com.lj.nettysocket.javaclient.config.IMClientConfig;
import com.lj.nettysocket.javaclient.handle.ClientHandler;
import com.lj.nettysocket.proto.Msg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.io.IOException;
import java.util.Scanner;

/**
 * @Author lj
 * @Date 2018/6/13 14:22
 */
public class IMClient implements Runnable,IMClientConfig {
    private ClientHandler clientHandler = new ClientHandler();
    public static void main(String[] args) throws IOException {
        new IMClient().start();
    }

    public void start() throws IOException{
        new Thread(this).start();
        runClientCMD();
    }
    public void runClientCMD() throws IOException{
        Msg.Builder s = Msg.newBuilder();
        s.setMsgType(TYPE_MSG_TEXT.getValue()).setReceiveId(DEFAULT_RECEIVE_ID).setUid(UID).setMsg(MSG_DEFAULT).build();
        Scanner scanner = new Scanner(System.in);
        do{
            s.setMsg(scanner.nextLine());
        }
        while (clientHandler.sendMsg(s.build()));
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // ----Protobuf处理器，这里的配置是关键----
                            ch.pipeline().addLast("frameDecoder", new ProtobufVarint32FrameDecoder());// 用于decode前解决半包和粘包问题（利用包头中的包含数组长度来识别半包粘包）
                            //配置Protobuf解码处理器，消息接收到了就会自动解码，ProtobufDecoder是netty自带的，Message是自己定义的Protobuf类
                            ch.pipeline().addLast("protobufDecoder", new ProtobufDecoder(Msg.getDefaultInstance()));
                            // 用于在序列化的字节数组前加上一个简单的包头，只包含序列化的字节长度。
                            ch.pipeline().addLast("frameEncoder", new ProtobufVarint32LengthFieldPrepender());
                            //配置Protobuf编码器，发送的消息会先经过编码
                            ch.pipeline().addLast("protobufEncoder", new ProtobufEncoder());
                            // ----Protobuf处理器END----
                            ch.pipeline().addLast(clientHandler);
                        }
                    });
            ChannelFuture f = b.connect(SERVER_HOST, SERVER_PORT).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
