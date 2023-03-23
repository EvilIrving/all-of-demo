package com.ygkj.gragh.controller;

import com.ygkj.gragh.WaterRainStatisticControllerApi;
import com.ygkj.gragh.service.WaterRainStatisticService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Date 2021/2/23
 */
@RestController
@RequestMapping("waterRainStatistic")
public class WaterRainStatisticController implements WaterRainStatisticControllerApi {

    @Autowired
    WaterRainStatisticService waterRainStatisticService;

    @GetMapping("statisticStationNumByAdcd")
    @Override
    public CommonResult statisticStationNumByAdcd(@RequestParam("stType") String stType) {
        return CommonResult.success(waterRainStatisticService.statisticStationNumByAdcd(stType));
    }

    @GetMapping("countStTypeNumByAdcdBasin")
    @Override
    public CommonResult countStTypeNumByAdcdBasin(@RequestParam(value = "adcd", required = false) String adcd,
                                                  @RequestParam(value = "basin", required = false) String basin) {
        return CommonResult.success(waterRainStatisticService.countStTypeNumByAdcdBasin(adcd, basin));
    }
}
