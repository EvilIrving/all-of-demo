package com.ygkj.visualization.controller;

import com.ygkj.visualization.DisasterControllerApi;
import com.ygkj.visualization.service.DisasterStatisticService;
import com.ygkj.visualization.vo.request.DisasterPageQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disaster")
public class DisasterController implements DisasterControllerApi {

    private DisasterStatisticService disasterStatisticService;

    public DisasterController(@Autowired DisasterStatisticService disasterStatisticService) {
        this.disasterStatisticService = disasterStatisticService;
    }

    @PostMapping("/selectByCondition")
    @Override
    public CommonResult selectByCondition(DisasterPageQueryVo queryVo) {
        return CommonResult.success(disasterStatisticService.selectByCondition(queryVo));
    }

    @PostMapping("/load")
    @Override
    public CommonResult load(Integer id) {
        return CommonResult.success(disasterStatisticService.load(id));
    }

    @GetMapping("/top5")
    @Override
    public CommonResult top5Disaster() {
        return CommonResult.success(disasterStatisticService.top5Disaster());
    }

    @GetMapping("/statistic")
    @Override
    public CommonResult statistic() {
        return CommonResult.success(disasterStatisticService.statistic());
    }
}
