package com.dcy.datamanage.biz;

import com.ygkj.jwt.TokenSettings;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.dcy")
@EnableScheduling
@EnableAsync
@MapperScan(basePackages = "com.dcy.datamanage.biz.mapper")
@EnableFeignClients
@Import(TokenSettings.class)
public class DataManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataManageApplication.class, args);
    }

}
