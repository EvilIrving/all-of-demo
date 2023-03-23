package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.service.SocialResourcesService;
import com.ygkj.big.screen.vo.request.SocialResourcesReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "社会资源")
@RestController
@RequestMapping("/socialResources")
public class SocialResourcesController {
    @Autowired
    private SocialResourcesService socialResourcesService;

    @PostMapping("countByType")
    public CommonResult countByType(SocialResourcesReqVo reqVo){
        return CommonResult.success(socialResourcesService.countByType(reqVo));
    }

    @PostMapping("list")
    public CommonResult list(SocialResourcesReqVo reqVo){
        return CommonResult.success(socialResourcesService.list(reqVo));
    }
}
