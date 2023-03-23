package com.ygkj.river.feign;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lxl
 * @create 2021-08-11 9:48
 * @description 强监管feign 主要提供县级用户列表服务
 */
@FeignClient(contextId = "supervision-service", name = "supervision-service")
public interface SupervisionFeignClient {

    /**
     * 查询包括用户的部门树
     *
     * @param key
     * @return
     */
    @ApiOperation("查询包括县级用户的部门树")
    @GetMapping("/user/getDeptTreeByTree")
    CommonResult getDeptTreeAboutUser(@RequestParam(value = "key", required = false) String key);

}
