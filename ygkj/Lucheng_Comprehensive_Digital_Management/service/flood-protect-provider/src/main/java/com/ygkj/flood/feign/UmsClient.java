package com.ygkj.flood.feign;

import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface UmsClient {

    @PostMapping("/file/load")
    CommonResult load(@RequestParam("ids") String ids);

}
