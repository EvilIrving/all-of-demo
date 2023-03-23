package com.ygkj.digitization.controller;

import com.ygkj.digitization.DroughtAnalysisControllerApi;
import com.ygkj.digitization.service.DroughtAnalysisService;
import com.ygkj.digitization.vo.request.DroughtReqVo;
import com.ygkj.digitization.vo.request.RainfallReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("droughtAnalysis")
public class DroughtAnalysisController implements DroughtAnalysisControllerApi {

    @Resource
    private DroughtAnalysisService droughtAnalysisService;

    @Override
    @PostMapping("listRainfall")
    public CommonResult listRainfall(RainfallReqVo reqVo) {
        return CommonResult.success(droughtAnalysisService.listRainfall(reqVo));
    }


    @Override
    @PostMapping("/droughtWraning")
    public CommonResult droughtWarning(DroughtReqVo reqVo) {
        return CommonResult.success(droughtAnalysisService.droughtWarning(reqVo));
    }


    @Override
    @GetMapping("/getDroughtStandard")
    public CommonResult getDroughtStandard() {
        return CommonResult.success(droughtAnalysisService.getDroughtStandard());
    }

    @Override
    @GetMapping("listDayRainfall")
    public CommonResult listDayRainfall() {
        return CommonResult.success(droughtAnalysisService.listDayRainfall());
    }
}
