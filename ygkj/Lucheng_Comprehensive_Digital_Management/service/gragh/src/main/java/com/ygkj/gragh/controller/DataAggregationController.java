package com.ygkj.gragh.controller;

import com.ygkj.gragh.DataAggregationControllerApi;
import com.ygkj.gragh.service.DataAggregationService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dataAggregation")
public class DataAggregationController implements DataAggregationControllerApi {

    private DataAggregationService dataAggregationService;

    public DataAggregationController(@Autowired DataAggregationService dataAggregationService) {
        this.dataAggregationService = dataAggregationService;
    }

    @PostMapping("/classificationStatistics")
    @Override
    public CommonResult ClassificationStatistics(String type) {
        return CommonResult.success(dataAggregationService.ClassificationStatistics(type));
    }

    @PostMapping("/onlineRateStatistics")
    @Override
    public CommonResult onlineRateStatistics(String type, String year) {
        return CommonResult.success(dataAggregationService.onlineRateStatistics(type, year));
    }

    @GetMapping("/cBit")
    @Override
    public CommonResult cBit() {
        return CommonResult.success(dataAggregationService.cBit());
    }
}
