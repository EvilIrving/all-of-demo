package com.ygkj.mybatis.configurations;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.ygkj.mybatis.interceptors.CustomDepartmentAutoInsertInterceptor;
import com.ygkj.mybatis.interceptors.DepartmentAutoInsertInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class MybatisCustomInterceptorAutoConfiguration {

    @Bean
    public ConfigurationCustomizer configurationCustomizer(@Autowired CustomDepartmentAutoInsertInterceptor insertInterceptor,
                                                           @Autowired DepartmentAutoInsertInterceptor interceptor) {
        return configuration -> {
            configuration.addInterceptor(interceptor);
//            configuration.addInterceptor(insertInterceptor);
        };
    }
}
