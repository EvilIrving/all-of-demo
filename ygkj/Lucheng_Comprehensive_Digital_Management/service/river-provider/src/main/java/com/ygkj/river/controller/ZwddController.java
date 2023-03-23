package com.ygkj.river.controller;


import com.ygkj.river.utils.ZwDingDingUtil;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/zwdd")
@Api(tags = "政务钉钉")
public class ZwddController {

    @GetMapping("get_jsapi_token")
    @ApiOperation(value = "获取应用 ticket")
    public CommonResult get_jsapi_token() throws Exception {
        String token = ZwDingDingUtil.getToken();
        System.out.println(token);
        String ticket = ZwDingDingUtil.getTicket(token);
        System.out.println(ticket);
        return CommonResult.success(ticket);
    }
}
