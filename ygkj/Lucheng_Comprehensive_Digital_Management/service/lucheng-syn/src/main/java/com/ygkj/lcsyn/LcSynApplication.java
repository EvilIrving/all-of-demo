package com.ygkj.lcsyn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = "com.ygkj.lcsyn.mapper")
@ComponentScan(basePackages = "com.ygkj.lcsyn.utils")
@ComponentScan(basePackages = "com.ygkj.lcsyn.task")
public class LcSynApplication {

    public static void main(String[] args) {
        SpringApplication.run(LcSynApplication.class, args);
    }

}
