package com.ygkj.gateway.clients;

import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Huang.zh
 * @date 2020/10/13 14:34
 * @Description:
 */
@FeignClient("authentication-system")
public interface PermissionClient {

    @GetMapping("/sys/permission/getPermissionsByUserId")
    CommonResult getPermissionsByUserId(String userId);
}
