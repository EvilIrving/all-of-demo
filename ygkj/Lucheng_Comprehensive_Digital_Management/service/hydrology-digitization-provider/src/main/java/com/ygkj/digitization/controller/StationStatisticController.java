package com.ygkj.digitization.controller;

import com.ygkj.digitization.StationStatisticControllerApi;
import com.ygkj.digitization.service.RainAndWaterService;
import com.ygkj.digitization.vo.request.AverageWaterAndRainQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stationStatistic")
public class StationStatisticController implements StationStatisticControllerApi {


    @Autowired
    private RainAndWaterService rainAndWaterService;

    @PostMapping("/stationAverageRecords")
    @Override
    public CommonResult stationAverageRecords(AverageWaterAndRainQueryVo queryVo) {
        return CommonResult.success(rainAndWaterService.stationAverageRecords(queryVo));
    }
}
