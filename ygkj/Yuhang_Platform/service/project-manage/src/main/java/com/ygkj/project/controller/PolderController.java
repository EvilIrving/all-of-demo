package com.ygkj.project.controller;

import com.ygkj.project.api.vo.res.PolderRangeResVo;
import com.ygkj.project.service.PolderService;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.utils.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("polder")
@Api(tags = "圩区")
public class PolderController {

    @Autowired
    private PolderService polderService;

    @ApiOperation(value = "带范围线的圩区列表",response = PolderRangeResVo.class)
    @GetMapping("listRange")    
    public CommonResult<PageVO<PolderRangeResVo>> listRange(String adcd){
       return CommonResult.success(polderService.listRange(adcd));
    }
}
