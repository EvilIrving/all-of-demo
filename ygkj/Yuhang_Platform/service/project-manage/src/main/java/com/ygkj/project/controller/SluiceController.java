package com.ygkj.project.controller;


import com.ygkj.project.api.vo.res.RelSluiceResVo;
import com.ygkj.project.service.SluiceService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sluice")
@Api(tags = "水闸")
public class SluiceController {

    @Autowired
    private SluiceService sluiceService;

    @ApiOperation(value = "根据prcd查询详情",response = RelSluiceResVo.class)
    @GetMapping("selectByPrcd")
    public CommonResult selectByPrcd(String prcd){
        return CommonResult.success(sluiceService.selectByPrcd(prcd));
    }
}
