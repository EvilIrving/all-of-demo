package com.ygkj.project.controller;

import com.ygkj.project.api.vo.res.RelGatestationResVo;
import com.ygkj.project.service.GateStationService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gateStation")
@Api(tags = "闸站")
public class GateStationController {

    @Autowired
    private GateStationService gateStationService;

    @ApiOperation(value = "根据prcd查询详情",response = RelGatestationResVo.class)
    @GetMapping("selectByPrcd")
    public CommonResult selectByPrcd(String prcd){
        return CommonResult.success(gateStationService.selectByPrcd(prcd));
    }
}
