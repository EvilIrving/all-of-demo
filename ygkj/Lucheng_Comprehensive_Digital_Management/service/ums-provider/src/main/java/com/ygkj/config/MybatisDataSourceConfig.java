package com.ygkj.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author Huang.zh
 * @date 2020/9/3 14:25
 * @Description: 数据源配置，如果服务端集成了日志，需要通过该配置类为Mybatis指定业务数据源，默认不会被加载
 */
//@Configuration
public class MybatisDataSourceConfig {
    @Bean(name = "PrimaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    @Primary
    public DataSource testDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "PrimarySqlSessionFactory")
    @Primary
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("PrimaryDataSource") DataSource datasource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return bean.getObject();// 设置mybatis的xml所在位置
    }


    @Bean("PrimarySqlSessionTemplate")
    // 表示这个数据源是默认数据源
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate(
            @Qualifier("PrimarySqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }
}
