package com.ygkj.water.project.controller;

import com.ygkj.project.model.FloodControlAndPresentation;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.EmergencySummaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emergencySummary")
public class EmergencySummaryController {
    @Autowired
    private EmergencySummaryService summaryService;

    @ApiOperation("应急总结查询")
    @PostMapping("/loadPage")
    public CommonResult loadPage(FloodControlAndPresentation entity){
        try{
            return CommonResult.success(summaryService.loadPage(entity));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @ApiOperation("应急总结删除")
    @PostMapping("/del")
    public CommonResult del(FloodControlAndPresentation entity){
        if (summaryService.delete(entity)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
