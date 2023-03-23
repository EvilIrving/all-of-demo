package com.ygkj.gragh;

import com.ygkj.annotations.EnableRsvrComputeService;
import com.ygkj.annotations.EnableSnowFlake;
import com.ygkj.exception.annotation.EnableGlobalExceptionHandler;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.swagger.config.Swagger2Config;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Huang.zh
 * @date 2020/11/23 15:48
 * @Description: 启动类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({Swagger2Config.class, TokenSettings.class})
@MapperScan("com.ygkj.gragh.mapper")
@EnableFeignClients
@EnableSnowFlake
@EnableGlobalExceptionHandler
//@EnableDiscoveryClient
@EnableRsvrComputeService
@EnableDubbo
@EnableScheduling
public class GraphApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphApplication.class, args);
    }

}
