package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.model.HealthCodeColor;
import com.ygkj.big.screen.model.HealthCodeWeight;
import com.ygkj.big.screen.service.HealthCodeService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("healthCode")
@Api(tags = "健康码配置")
public class HealthCodeController {

    @Resource
    private HealthCodeService healthCodeService;

    @PostMapping("saveWeight")
    @ApiOperation("保存权重")
    public CommonResult saveWeight(@RequestBody List<HealthCodeWeight> list){
        healthCodeService.saveWeight(list);
        return CommonResult.success();
    }

    @GetMapping("listWeight/{seawallCode}")
    @ApiOperation("查询权重")
    public CommonResult listWeight(@PathVariable("seawallCode") String seawallCode){
        return CommonResult.success(healthCodeService.listHealthCodeWeight(seawallCode));
    }

    public CommonResult listSubWeight(@PathVariable("seawallCode") String parentId){
        return CommonResult.success(healthCodeService.listSubWeight(parentId));
    }

    @PostMapping("saveColor")
    @ApiOperation("保存码分")
    public CommonResult saveColor(@RequestBody List<HealthCodeColor> list){
        healthCodeService.saveColor(list);
        return CommonResult.success();
    }

    @GetMapping("listColor/{seawallCode}")
    @ApiOperation("查询码分")
    public CommonResult listColor(@PathVariable("seawallCode") String seawallCode){
        return CommonResult.success(healthCodeService.listColor(seawallCode));
    }

}
