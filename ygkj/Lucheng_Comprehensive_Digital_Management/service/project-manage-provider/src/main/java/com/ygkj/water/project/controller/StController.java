package com.ygkj.water.project.controller;

import com.ygkj.project.model.AttStBase;
import com.ygkj.project.vo.request.AttStBaseReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.StService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("st")
@Api(tags = "测站管理")
public class StController {

    @Resource
    private StService stService;

    @ApiOperation(value = "分页查询测站信息",response = AttStBase.class)
    @PostMapping("page")
    public CommonResult page(AttStBaseReqVo reqVo){
        return CommonResult.success(stService.page(reqVo));
    }

    @ApiOperation("更新测站信息")
    @PostMapping("update")
    public CommonResult update(AttStBase attStBase){
        stService.update(attStBase);
        return CommonResult.success();
    }
}
