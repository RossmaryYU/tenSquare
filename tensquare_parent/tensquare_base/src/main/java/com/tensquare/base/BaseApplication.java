package com.tensquare.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

/**
 * 基础微服务启动类
 */
@SpringBootApplication
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class,args);
    }

    /**
     * 初始化IdWork
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
