package com.ygkj.digitization.controller;

import com.ygkj.digitization.MaxValueControllerApi;
import com.ygkj.digitization.service.RainAndWaterService;
import com.ygkj.digitization.vo.request.MaxWaterLevelQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/maxValue")
public class MaxValueController implements MaxValueControllerApi {

    @Autowired
    private RainAndWaterService rainAndWaterService;

    @PostMapping("/maxValues")
    @Override
    public CommonResult maxValues(MaxWaterLevelQueryVo queryVo) {
        return CommonResult.success(rainAndWaterService.stationMaxWaterLevel(queryVo));
    }
}
