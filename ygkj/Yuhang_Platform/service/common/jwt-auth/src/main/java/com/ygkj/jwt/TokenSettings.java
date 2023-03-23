package com.ygkj.jwt;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @ClassName: TokenSettings
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/7 20:46
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/7 20:46
 * @Version: 0.0.1
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class TokenSettings implements InitializingBean {
    private String secretKey;
    private Duration accessTokenExpireTime;
    private Duration refreshTokenExpireTime;
    private Duration refreshTokenExpireAppTime;
    private String  issuer;


    @Override
    public String toString() {
        return "TokenSettings{" +
                "secretKey='" + secretKey + '\'' +
                ", accessTokenExpireTime=" + accessTokenExpireTime +
                ", refreshTokenExpireTime=" + refreshTokenExpireTime +
                ", refreshTokenExpireAppTime=" + refreshTokenExpireAppTime +
                ", issuer='" + issuer + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("===========================================");
        System.out.println(toString());
        if (JwtTokenUtil.compareAndSet()){
            System.out.println("111111111111111111111111111111111111111111111111");
            JwtTokenUtil.setTokenSettings(this);
        }
        System.out.println("===========================================");
    }
}
