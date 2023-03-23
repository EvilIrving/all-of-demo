package com.ygkj.gragh.feign;


import com.ygkj.gragh.model.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface UserFeignClient {

    @GetMapping("/sys/users")
    List<SysUser> users(@RequestParam(value = "ids", required = false) String ids);
}
