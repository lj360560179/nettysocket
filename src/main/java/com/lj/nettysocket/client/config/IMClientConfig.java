package com.lj.nettysocket.client.config;

import com.lj.nettysocket.struct.MessageType;

/**
 * @Author lj
 * @Date 2018/6/13 14:24
 */
public interface IMClientConfig {
    /**客户端配置*/
    int CLIENT_VERSION = 1;         //版本号
    /**服务端配置*/
    String SERVER_HOST = "127.0.0.1";  //服务器IP
    int  SERVER_PORT = 9090;           //服务器端口
    /**消息相关*/
    byte APP_IM = 1;                //即时通信应用ID为1

    int UID = 8888;
    int DEFAULT_RECEIVE_ID = 9999;

    MessageType TYPE_MSG_AUTH = MessageType.TYPE_AUTH;         //连接后第一次消息确认建立连接和发送认证信息
    MessageType TYPE_MSG_TEXT = MessageType.TYPE_TEXT;         //文本消息
    String MSG_DEFAULT = "";          //默认为空消息
}