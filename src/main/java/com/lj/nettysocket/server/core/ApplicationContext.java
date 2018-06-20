package com.lj.nettysocket.server.core;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author lj
 * @Date 2018/6/13 14:09
 * 功能比较简单，主要用来保存登录用户信息，以Map来存储，
 * 其中key为用户ID，value为客户端对应的ChannelHandlerContext对象。
 */
public class ApplicationContext {

    public static ConcurrentHashMap<Integer, ChannelHandlerContext> onlineUsers = new ConcurrentHashMap<Integer, ChannelHandlerContext>();

    public static void add(Integer uid, ChannelHandlerContext ctx) {
        onlineUsers.put(uid, ctx);
    }

    public static void remove(Integer uid) {
        onlineUsers.remove(uid);
    }

    public static ChannelHandlerContext getContext(Integer uid) {
        return onlineUsers.get(uid);
    }
}
