package com.ygkj.visualization.controller;

import com.ygkj.visualization.SurfaceDataControllerApi;
import com.ygkj.visualization.service.RainDataSurfaceService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@RequestMapping("/surfaceData")
public class SurfaceDataController implements SurfaceDataControllerApi {

    private RainDataSurfaceService rainDataSurfaceService;

    public SurfaceDataController(@Autowired RainDataSurfaceService rainDataSurfaceService) {
        this.rainDataSurfaceService = rainDataSurfaceService;
    }

    @GetMapping("/fiveKilometerCorrectRainData")
    @Override
    public CommonResult fiveKilometerCorrectRainData() {
        return CommonResult.success(rainDataSurfaceService.get5kmCorrectRainData());
    }
}
