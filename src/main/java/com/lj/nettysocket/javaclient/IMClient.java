package com.lj.nettysocket.javaclient;

import com.lj.nettysocket.javaclient.config.IMClientConfig;
import com.lj.nettysocket.javaclient.handle.ClientHandler;
import com.lj.nettysocket.codec.JsonDecode;
import com.lj.nettysocket.codec.JsonEncode;
import com.lj.nettysocket.struct.PMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
        PMessage message = new PMessage(UID, TYPE_MSG_TEXT.getValue(), DEFAULT_RECEIVE_ID, MSG_DEFAULT);
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
                           // ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                            ch.pipeline().addLast("json decoder",new JsonDecode());
                           //  ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                            ch.pipeline().addLast("json encoder",new JsonEncode());
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
