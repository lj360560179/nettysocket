
package com.lj.nettysocket.proto;

public interface MsgOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.protobuf.Msg)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 uid = 1;</code>
   */
  int getUid();

  /**
   * <code>int32 receiveId = 2;</code>
   */
  int getReceiveId();

  /**
   * <code>string msgType = 3;</code>
   */
  String getMsgType();
  /**
   * <code>string msgType = 3;</code>
   */
  com.google.protobuf.ByteString
      getMsgTypeBytes();

  /**
   * <code>string msg = 4;</code>
   */
  String getMsg();
  /**
   * <code>string msg = 4;</code>
   */
  com.google.protobuf.ByteString
      getMsgBytes();
}
