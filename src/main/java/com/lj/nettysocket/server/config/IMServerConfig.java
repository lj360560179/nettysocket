package com.lj.nettysocket.server.config;

import com.lj.nettysocket.struct.MessageType;

/**
 * @Author lj
 * @Date 2018/6/13 14:10
 */
public interface IMServerConfig {

    /**
     * 服务端配置
     * 服务器IP
     */
    String SERVER_HOST = "127.0.0.1";

    /**
     * 服务器端口
     */
    int SERVER_PORT = 9090;

    /**
     * 消息相关,表示服务器消息
     */
    int SERVER_ID = 0;


    /**
     * 连接后第一次消息确认建立连接和发送认证信息
     */
    MessageType TYPE_MSG_CONNECT = MessageType.TYPE_AUTH;

    /**
     * 文本消息
     */
    MessageType TYPE_MSG_TEXT = MessageType.TYPE_TEXT;

    /**
     * 空消息
     */
    String MSG_DEFAULT = "";
}
