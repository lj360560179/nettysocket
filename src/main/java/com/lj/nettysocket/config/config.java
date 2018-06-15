package com.lj.nettysocket.config;

import com.lj.nettysocket.server.IMServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lj
 * @Date 2018/6/13 15:03
 */
@Configuration
public class config {

    /**
     * 配置
     * @return
     * @throws Exception
     */
    @Bean
    public IMServer initIMServer() throws Exception{
        IMServer server = new IMServer();
        server.start();
        return server;
    }
}
