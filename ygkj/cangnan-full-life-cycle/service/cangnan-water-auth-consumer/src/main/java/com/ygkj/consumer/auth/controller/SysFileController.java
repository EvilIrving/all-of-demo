package com.ygkj.consumer.auth.controller;

import com.ygkj.consumer.auth.client.authentication.SysFileClient;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/file")
@Api(tags = "文件查询服务",value = "文件查询服务")
public class SysFileController {

    @Resource
    private SysFileClient sysFileClient;

    @PostMapping("load")
    @ApiOperation("查询文件")
    private CommonResult load(String ids) {
        return CommonResult.success(sysFileClient.load(ids));
    }
}
