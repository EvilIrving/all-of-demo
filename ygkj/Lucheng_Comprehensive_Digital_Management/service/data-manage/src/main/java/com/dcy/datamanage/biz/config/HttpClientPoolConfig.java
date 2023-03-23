package com.dcy.datamanage.biz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "httpclient")
@Getter
@Setter
public class HttpClientPoolConfig {

    private Integer connectTimeout;
    private Integer readTimeout;
    private Integer requestTimeout;
    private Integer maxTotal;
    private Integer maxPerRoute;
    private Integer retryTimes;
    private Integer maxIdleTime;

}
