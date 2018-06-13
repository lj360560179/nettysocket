package com.lj.nettysocket.client.handle;

/**
 * @Author lj
 * @Date 2018/6/13 14:21
 */
public class ClientHandler extends ChannelInboundHandlerAdapter implements IMClientConfig{
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("用户["+ UID + "]成功连接服务器");
        this.ctx = ctx;

        //通道建立时发送认证消息给服务器
        IMMessage message = new IMMessage(
                APP_IM,
                CLIENT_VERSION,
                UID,
                TYPE_MSG_AUTH.value(),
                DEFAULT_RECEIVE_ID,
                MSG_DEFAULT);
        sendMsg(message);
    }

    public boolean sendMsg(IMMessage msg) throws IOException {
        boolean result = msg.getMsg().equals("quit") ? false:true;
        if(result){
            /*if(msg.getMsgType() != MessageType.TYPE_AUTH.value()){
                System.out.println("client[" + msg.getUid() + "]:" + msg.getMsg());
            }*/
            ctx.writeAndFlush(msg);
        }
        return result;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMMessage m = (IMMessage)msg;
        System.out.println("receive[" + m.getUid() + "]:" + m.getMsg());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("与服务器断开连接：" + cause.getMessage());
        ctx.close();
    }
}
