package com.ygkj.visualization.controller;

import com.ygkj.visualization.SatelliteCloudChartControllerApi;
import com.ygkj.visualization.service.SatelliteCloudChartService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/satelliteCloudChart")
public class SatelliteCloudChartController implements SatelliteCloudChartControllerApi {

    private SatelliteCloudChartService satelliteCloudChartService;

    public SatelliteCloudChartController(@Autowired SatelliteCloudChartService satelliteCloudChartService) {
        this.satelliteCloudChartService = satelliteCloudChartService;
    }

    @GetMapping("")
    @Override
    public CommonResult satelliteCloudChart() {
        return CommonResult.success(satelliteCloudChartService.satelliteCloudChart());
    }
}
