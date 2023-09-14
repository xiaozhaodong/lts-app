package com.shaw.lts.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * LtsAppApplication
 * lts-app启动
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/21 14:38
 **/
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@MapperScan(basePackages = {
    "com.shaw.lts.persist.mapper",
    "com.shaw.lts.adapter.service.dao"
})
@ComponentScans(value = {
    @ComponentScan("com.shaw.lts.core.*"),
    @ComponentScan("com.shaw.lts.adapter.service.impl"),
    @ComponentScan("com.shaw.lts.handle.*")
//    @ComponentScan("com.newland.llas.feign.config")
})
public class LtsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LtsAppApplication.class, args);
    }
}
