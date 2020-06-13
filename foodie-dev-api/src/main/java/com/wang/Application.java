package com.wang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//扫描 mybatis 通用 mapper 所在的包
@MapperScan(basePackages = "com.wang.mapper")
//扫描所有包和相关组建的包
@ComponentScan(basePackages = {"com.wang","org.n3r.idworker"})
/*@EnableScheduling //开启定时器*/
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
