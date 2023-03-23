package com.ygkj.visualization.controller;

import com.ygkj.visualization.SiteControllerApi;
import com.ygkj.visualization.service.SiteService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/site")
public class SiteController implements SiteControllerApi {

    @Autowired
    @Qualifier("windFarmService")
    private SiteService windFarmService;
    @Autowired
    @Qualifier("waveFieldService")
    private SiteService waveFieldService;

    @GetMapping("/windFarmsTimeList")
    @Override
    public CommonResult windFarmList() {
        return CommonResult.success(windFarmService.siteTimeList());
    }

    @GetMapping("/wind/{id}")
    @Override
    public CommonResult windFarm(@PathVariable Integer id) {
        return CommonResult.success(windFarmService.loadWindFarm(id));
    }

    @GetMapping("/waveFieldList")
    @Override
    public CommonResult waveFieldList() {
        return CommonResult.success(waveFieldService.siteTimeList());
    }

    @GetMapping("/wave/{id}")
    @Override
    public CommonResult waveField(@PathVariable Integer id) {
        return CommonResult.success(waveFieldService.loadWindFarm(id.intValue()));
    }
}
