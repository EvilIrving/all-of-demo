package com.ygkj.visualization.controller;

import com.ygkj.visualization.MeteorologicalControllerApi;
import com.ygkj.visualization.service.MeteorologicalService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meteorological")
public class MeteorologicalController implements MeteorologicalControllerApi {

    private MeteorologicalService meteorologicalService;

    public MeteorologicalController(@Autowired MeteorologicalService meteorologicalService) {
        this.meteorologicalService = meteorologicalService;
    }

    @GetMapping
    @Override
    public CommonResult meteorologicalRealTimeData() {
        return CommonResult.success(meteorologicalService.meteorologicalRealTimeData());
    }

    @PostMapping("/meteorologicalPastTimeData")
    @Override
    public CommonResult meteorologicalPastTimeData(String stationCode, Integer days) {
        return CommonResult.success(meteorologicalService.meteorologicalPastTimeData(stationCode, days));
    }
}
