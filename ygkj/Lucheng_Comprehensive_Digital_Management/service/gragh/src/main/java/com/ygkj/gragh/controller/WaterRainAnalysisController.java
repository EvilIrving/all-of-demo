package com.ygkj.gragh.controller;

import com.ygkj.gragh.WaterRainAnalysisControllerApi;
import com.ygkj.gragh.service.WaterRainAnalysisService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Date 2021/2/22
 */
@RestController
@RequestMapping("/waterRainAnalysis")
public class WaterRainAnalysisController implements WaterRainAnalysisControllerApi {

    @Autowired
    WaterRainAnalysisService waterRainAnalysisService;

    @GetMapping("selectWaterRainSimpleReport")
    @Override
    public CommonResult selectWaterRainSimpleReport() {
        return CommonResult.success(waterRainAnalysisService.selectWaterRainSimpleReport());
    }

    @GetMapping("rainfallOverview")
    @Override
    public CommonResult rainfallOverview() {
        return CommonResult.success(waterRainAnalysisService.rainfallOverview());
    }

    @GetMapping("riverOverview")
    @Override
    public CommonResult riverOverview() {
        return CommonResult.success(waterRainAnalysisService.riverOverview());
    }

    @GetMapping("largeMidResOverview")
    @Override
    public CommonResult largeMidResOverview() {
        return CommonResult.success(waterRainAnalysisService.largeMidResOverview());
    }

    @GetMapping("selectHistoryExtrem")
    @Override
    public CommonResult selectHistoryExtrem(@RequestParam("type") String type, @RequestParam(value = "stName", required = false) String stName) {
        return CommonResult.success(waterRainAnalysisService.selectHistoryExtrem(type, stName));
    }
}
