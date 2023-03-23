package com.ygkj.big.screen;


import com.ygkj.big.screen.unit.notify.event.UnitCacheEvent;
import com.ygkj.annotations.EnableSnowFlake;
import com.ygkj.big.screen.bootstrap.GisExtend;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.swagger.config.Swagger2Config;
import com.ygkj.water.exception.GlobalExceptionHandler;
import feign.Feign;
import feign.Retryer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableSnowFlake
@SpringBootApplication
@MapperScan(basePackages = "com.ygkj.big.screen.mapper")
@Import({Swagger2Config.class, TokenSettings.class, GlobalExceptionHandler.class})
@EnableFeignClients
@EnableScheduling
public class BigScreenApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BigScreenApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean("gisExtend")
    public GisExtend getGisExtend() {
        return new GisExtend();
    }

    @Override
    public void run(String... args) throws Exception {
        NotifyCenter.publishEvent(new UnitCacheEvent());
    }

    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    @Primary
    public Feign.Builder feignBuilder() {
        return Feign.builder().retryer(feignRetryer());
    }
}
