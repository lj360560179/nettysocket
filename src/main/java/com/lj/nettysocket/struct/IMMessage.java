package com.lj.nettysocket.struct;

import org.msgpack.annotation.Message;

/**
 * @Author lj
 * @Date 2018/6/13 14:23
 */
@Message
public class IMMessage {
    //应用ID
    private byte appId;

    //版本
    private int version;

    //用户ID
    private int uid;

    //消息类型 0：登录 1：文字消息
    private byte msgType;

    //接收方
    private int receiveId;

    //消息内容
    private String msg;

    public IMMessage(){

    }

    /**
     * 构造方法
     * @param appId     应用通道
     * @param version   应用版本
     * @param uid       用户ID
     * @param msgType   消息类型
     * @param receiveId 消息接收者
     * @param msg       消息内容
     */
    public IMMessage(byte appId, int version, int uid, byte msgType, int receiveId, String msg) {
        this.appId = appId;
        this.version = version;
        this.uid = uid;
        this.msgType = msgType;
        this.receiveId = receiveId;
        this.msg = msg;
    }

    public byte getAppId() {
        return appId;
    }

    public void setAppId(byte appId) {
        this.appId = appId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
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
                "appId=" + appId +
                ", version=" + version +
                ", uid=" + uid +
                ", msgType=" + msgType +
                ", receiveId=" + receiveId +
                ", msg='" + msg + '\'' +
                '}';
    }
}