package com.ygkj.flood.newVersion.controller;

import com.ygkj.flood.WaterAndRainControllerApi;
import com.ygkj.flood.service.WaterAndRainService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waterAndRain")
public class WaterAndRainController implements WaterAndRainControllerApi {

    @Autowired
    private WaterAndRainService waterAndRainService;

    @GetMapping("/topNRes")
    @Override
    public CommonResult topNReservoir(Integer index) {
        return CommonResult.success(waterAndRainService.topNReservoir(index));
    }

    @GetMapping("/reservoirWaterAndRain")
    @Override
    public CommonResult BigAndMidReservoirWaterStation(String basin) {
        return CommonResult.success(waterAndRainService.BigAndMidReservoirWaterStation(basin));
    }

    @GetMapping("/rainfallTop10")
    @Override
    public CommonResult rainfallTop10(Integer hours) {
        return CommonResult.success(waterAndRainService.rainfallTop10(hours));
    }

    @GetMapping("/beyondNumber")
    @Override
    public CommonResult beyondLimitLevelNumber() {
        return CommonResult.success(waterAndRainService.beyondLimitLevelNumber());
    }

    @GetMapping("/superRainNumber")
    @Override
    public CommonResult superRainNumber() {
        return CommonResult.success(waterAndRainService.superRainNumber());
    }
}
