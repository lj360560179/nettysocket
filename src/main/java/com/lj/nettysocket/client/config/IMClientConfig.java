package com.lj.nettysocket.client.config;

import com.lj.nettysocket.struct.MessageType;

/**
 * @Author lj
 * @Date 2018/6/13 14:24
 */
public interface IMClientConfig {

    /**
     * 服务端配置
     */
    String SERVER_HOST = "127.0.0.1";  //服务器IP
    /**
     * 服务器端口
     */
    int SERVER_PORT = 9090;

    /**
     *
     */
    int UID = 1;

    /**
     *
     */
    int DEFAULT_RECEIVE_ID = 2;

    /**
     * 连接后第一次消息确认建立连接和发送认证信息
     */
    MessageType TYPE_MSG_AUTH = MessageType.TYPE_AUTH;

    /**
     * 文本消息
     */
    MessageType TYPE_MSG_TEXT = MessageType.TYPE_TEXT;

    /**
     * 默认为空消息
     */
    String MSG_DEFAULT = "";
}