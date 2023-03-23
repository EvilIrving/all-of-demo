package com.ygkj.digitization;


import com.ygkj.annotations.EnableSnowFlake;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.swagger.config.Swagger2Config;
import com.ygkj.utils.SnowFlake;
import com.ygkj.water.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({Swagger2Config.class, TokenSettings.class, GlobalExceptionHandler.class})
@MapperScan(basePackages = "com.ygkj.digitization.mapper")
@EnableScheduling
@EnableFeignClients
@EnableSnowFlake
public class HydrologyDigitizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydrologyDigitizationApplication.class,args);
    }

    @Bean
    public SnowFlake snowFlake(){
        return new SnowFlake(03l,04l);
    }
}
