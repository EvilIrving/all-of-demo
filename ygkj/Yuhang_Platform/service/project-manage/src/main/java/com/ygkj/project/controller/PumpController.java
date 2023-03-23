package com.ygkj.project.controller;

import com.ygkj.project.api.vo.res.RelPumpResVo;
import com.ygkj.project.service.PumpService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pump")
@Api(tags = "泵站")
public class PumpController {

    @Autowired
    private PumpService pumpService;

    @ApiOperation(value = "根据prcd查询详情",response = RelPumpResVo.class)
    @GetMapping("selectByPrcd")
    public CommonResult selectByPrcd(String prcd){
        return CommonResult.success(pumpService.selectByPrcd(prcd));
    }
}
