package com.ygkj.water.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;

/**
 * @author Huang.zh
 * @date 2020/8/26 11:10
 * @Description: 用户认证服务feign抽象
 */
@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface AuthenticationClient {

    @GetMapping("/sys/depts")
    LinkedHashMap departments();
}
