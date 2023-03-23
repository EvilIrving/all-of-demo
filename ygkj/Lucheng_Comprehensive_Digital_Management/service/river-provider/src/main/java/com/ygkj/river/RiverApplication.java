package com.ygkj.river;

import com.ygkj.annotations.EnableSnowFlake;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.swagger.config.Swagger2Config;
import com.ygkj.water.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableSnowFlake
@EnableFeignClients
@MapperScan(basePackages = "com.ygkj.river.mapper")
@Import({Swagger2Config.class, TokenSettings.class, GlobalExceptionHandler.class})
public class RiverApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiverApplication.class, args);
    }
}
