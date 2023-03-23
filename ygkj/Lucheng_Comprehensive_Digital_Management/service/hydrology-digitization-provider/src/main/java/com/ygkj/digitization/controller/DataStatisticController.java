package com.ygkj.digitization.controller;

import com.ygkj.digitization.DataStatisticControllerApi;
import com.ygkj.digitization.service.RainAndWaterService;
import com.ygkj.digitization.vo.request.RelationStatisticQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dataStatistic")
public class DataStatisticController implements DataStatisticControllerApi {

    @Autowired
    private RainAndWaterService rainAndWaterService;

    @PostMapping("/relationStatistic")
    @Override
    public CommonResult relationStatistic(RelationStatisticQueryVo queryVo) {
        return CommonResult.success(rainAndWaterService.relationStatistic(queryVo));
    }
}
