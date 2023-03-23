package com.ygkj.soft.database;

import com.ygkj.jwt.TokenSettings;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Huang.zh
 * @date 2020/9/1 14:24
 * @Description: 数据审核中心启动类
 */
@EnableScheduling
@EnableSwagger2
@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoDataSourceProxy
//用于JWT解析用户相关信息
@Import({TokenSettings.class})
public class SoftDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftDatabaseApplication.class,args);
    }
}
