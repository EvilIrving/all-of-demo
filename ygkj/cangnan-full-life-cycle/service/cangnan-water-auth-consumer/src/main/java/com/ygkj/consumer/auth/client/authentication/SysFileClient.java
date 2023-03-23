package com.ygkj.consumer.auth.client.authentication;

import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Huang.zh
 * @date 2020/8/26 11:10
 * @Description: 用户认证服务feign抽象
 */
@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface SysFileClient {

    @PostMapping("/file/load")
    CommonResult load(@RequestParam("ids") String ids);
}
