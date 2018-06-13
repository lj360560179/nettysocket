package com.lj.nettysocket.client;

import com.lj.nettysocket.client.config.IMClientConfig;
import com.lj.nettysocket.client.handle.ClientHandler;
import com.lj.nettysocket.codec.MsgPackDecode;
import com.lj.nettysocket.codec.MsgPackEncode;
import com.lj.nettysocket.struct.IMMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

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
        IMMessage message = new IMMessage(
                APP_IM,
                CLIENT_VERSION,
                UID,
                TYPE_MSG_TEXT.value(),
                DEFAULT_RECEIVE_ID,
                MSG_DEFAULT);
        Scanner scanner = new Scanner(System.in);
        do{
            message.setMsg(scanner.nextLine());
        }
        while (clientHandler.sendMsg(message));
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
                            ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                            ch.pipeline().addLast("msgpack decoder",new MsgPackDecode());
                            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            ch.pipeline().addLast("msgpack encoder",new MsgPackEncode());
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
