package com.ygkj.lcsyn.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
public class JdbcTemplateConfig {

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Bean("bhJdbc")
    public JdbcTemplate bhJdbcTemplate(){
        return new JdbcTemplate(dynamicRoutingDataSource.getDataSource("bh"));
    }

    @Bean("exchangeJdbc")
    public JdbcTemplate exJdbcTemplate(){
        return new JdbcTemplate(dynamicRoutingDataSource.getDataSource("exchange"));
    }
}
