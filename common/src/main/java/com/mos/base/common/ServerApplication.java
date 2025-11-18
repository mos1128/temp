package com.mos.base.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 服务启动类
 *
 * @author ly
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.mos.base"})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
} 
