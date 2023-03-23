package com.ygkj.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.service.OtherService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/other")
@Api(tags = "其他信息接口")
public class OtherController {

    @Resource
    private OtherService otherService;

    @GetMapping("/managemaent/getWeather")
    @ApiOperation(value = "获取天气")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "行政区划编码(对应是天气网得code编码)")
    })
    public CommonResult getWeather(String areaCode) {
        if (StringUtils.isEmpty(areaCode)) {
            areaCode = "101210106";// 默认余杭
        }
        JSONObject jo = otherService.getWeather(areaCode);
        return CommonResult.success(jo);
    }

}
