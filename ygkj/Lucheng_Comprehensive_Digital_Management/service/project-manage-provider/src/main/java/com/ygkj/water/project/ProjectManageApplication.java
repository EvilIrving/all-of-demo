package com.ygkj.water.project;


import com.ygkj.annotations.EnableSnowFlake;
import com.ygkj.aspects.annotations.EnableAutoAssignment;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(value = "com.ygkj.water.project.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({Swagger2Config.class, TokenSettings.class})
@EnableFeignClients
@EnableAutoAssignment

@EnableScheduling
@EnableSnowFlake
@EnableAsync
public class ProjectManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManageApplication.class,args);
    }

    @Bean
    public SnowFlake snowFlake(){
        return new SnowFlake(11L,12L);
    }
}
