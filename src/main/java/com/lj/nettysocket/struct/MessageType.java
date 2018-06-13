package com.lj.nettysocket.struct;

/**
 * @Author lj
 * @Date 2018/6/13 14:24
 */
public enum MessageType {
    TYPE_AUTH((byte) 0), TYPE_LOGOUT((byte) 1), TYPE_TEXT((byte) 2), TYPE_EMPTY((byte) 3);
    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
