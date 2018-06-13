package com.lj.nettysocket.controller;

import com.lj.nettysocket.server.core.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lj
 * @Date 2018/6/13 15:17
 */
@RestController
public class UserController {

    @GetMapping("/users")
    public Mono<Object> push() {
        return Mono.justOrEmpty(ApplicationContext.onlineUsers);
    }


}
