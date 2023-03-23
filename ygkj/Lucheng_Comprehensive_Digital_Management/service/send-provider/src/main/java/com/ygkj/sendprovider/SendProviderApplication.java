package com.ygkj.sendprovider;

import com.ygkj.annotations.EnableSnowFlake;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.swagger.config.Swagger2Config;
import com.ygkj.water.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(value = "com.ygkj.sendprovider.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({Swagger2Config.class, TokenSettings.class, GlobalExceptionHandler.class})
@EnableSnowFlake
public class SendProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SendProviderApplication.class, args);
    }

}
