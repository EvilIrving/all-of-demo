package com.ygkj.lcsyn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = "com.ygkj.lcsyn.mapper")
public class LcSynApplication {

    public static void main(String[] args) {
        SpringApplication.run(LcSynApplication.class, args);
    }

}
