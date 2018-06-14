package com.lj.nettysocket.controller;

import com.lj.nettysocket.server.IMServer;
import com.lj.nettysocket.server.core.ApplicationContext;
import com.lj.nettysocket.struct.IMMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


/**
 * @Author lj
 * @Date 2018/6/13 15:17
 */
@RestController
public class PushController {

    @Autowired
    IMServer server;

    /**
     * 获取在线用户
     * @return
     */
    @GetMapping("/users")
    public Mono<Object> users() {
        return Mono.justOrEmpty(ApplicationContext.onlineUsers);
    }

    /**
     * 推送
     */
    @GetMapping("/push")
    public void push(){
        IMMessage msg = server.getMessage();
        msg.setMsg("push");
        System.out.println(server.sendMsg(msg));
    }


}
