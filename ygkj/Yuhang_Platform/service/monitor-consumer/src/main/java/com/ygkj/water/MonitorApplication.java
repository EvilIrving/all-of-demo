package com.ygkj.water;

import com.ygkj.swagger.config.Swagger2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

/**
 * @author: fml
 * @Date: 2020/9/8 18:26
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@EnableUserOperationLog
@Import(Swagger2Config.class)
public class MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }
}
