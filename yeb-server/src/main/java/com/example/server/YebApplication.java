package com.example.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 */

@SpringBootApplication
@MapperScan("com.example.server.mapper")
@EnableScheduling
public class YebApplication {
    public static void main(String[] args){
        SpringApplication.run(YebApplication.class , args);

    }

}
