package com.lj.nettysocket.struct;

import org.msgpack.annotation.Message;

/**
 * @Author lj
 * @Date 2018/6/13 14:23
 */
@Message
public class IMMessage {

    /**
     * 用户
     */
    private int uid;


    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 接收方
     */
    private int receiveId;

    /**
     * 消息内容
     */
    private String msg;

    public IMMessage(){}

    /**
     * 构造方法
     * @param uid       用户ID
     * @param msgType   消息类型
     * @param receiveId 消息接收者
     * @param msg       消息内容
     */
    public IMMessage(int uid, String msgType, int receiveId, String msg) {
        this.uid = uid;
        this.msgType = msgType;
        this.receiveId = receiveId;
        this.msg = msg;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(int receiveId) {
        this.receiveId = receiveId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                ", uid=" + uid +
                ", msgType=" + msgType +
                ", receiveId=" + receiveId +
                ", msg='" + msg + '\'' +
                '}';
    }
}