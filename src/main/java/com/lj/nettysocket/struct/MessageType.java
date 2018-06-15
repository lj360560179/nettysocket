package com.lj.nettysocket.struct;

/**
 * @Author lj
 * @Date 2018/6/13 14:24
 */
public enum MessageType {

    SYSTEM("系统消息"),
    TYPE_AUTH("认证消息"),
    TYPE_TEXT("文本消息");

    private String value;

    private MessageType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
