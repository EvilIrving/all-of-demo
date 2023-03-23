package com.ygkj.consumer.auth;

import com.ygkj.exception.annotation.EnableGlobalExceptionHandler;
import com.ygkj.swagger.config.Swagger2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author Huang.zh
 * @date 2020/8/26 11:15
 * @Description: 认证微服务调用方启动类
 */
//服务调用方去掉数据源的自动配置
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
//@EnableUserOperationLog
@Import(Swagger2Config.class)
@EnableGlobalExceptionHandler
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class,args);
    }
}
