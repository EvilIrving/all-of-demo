package com.ygkj.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(value = "com.ygkj.project.mapper")
public class ProjectManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManageApplication.class,args);
    }
}
