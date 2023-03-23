package com.ygkj.visualization;

import com.ygkj.annotations.EnableSnowFlake;
import com.ygkj.bootstrap.annotation.EnableGracefulStopBootstrap;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.swagger.config.Swagger2Config;
import com.ygkj.visualization.bootstrap.StopBootStrap;
import com.ygkj.water.exception.GlobalExceptionHandler;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@MapperScan("com.ygkj.visualization.mapper")
@Import({Swagger2Config.class, TokenSettings.class, GlobalExceptionHandler.class})
@EnableGracefulStopBootstrap(bootstrapClass = StopBootStrap.class)
@EnableSnowFlake
@EnableScheduling
@EnableDubbo
public class MeteorologicalVisualizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeteorologicalVisualizationApplication.class,args);
    }
}
