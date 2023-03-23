package com.ygkj.flood;

import com.ygkj.jwt.TokenSettings;
import com.ygkj.swagger.config.Swagger2Config;
import com.ygkj.utils.SnowFlake;
import com.ygkj.water.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan(basePackages = "com.ygkj.flood.mapper")
@Import({Swagger2Config.class, TokenSettings.class, GlobalExceptionHandler.class})
@EnableFeignClients
public class FloodProtectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloodProtectApplication.class);
    }

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(05l, 06l);
    }
}
