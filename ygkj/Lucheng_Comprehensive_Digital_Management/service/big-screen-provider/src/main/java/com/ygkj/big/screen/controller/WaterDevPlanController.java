package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterDevPlanControllerApi;
import com.ygkj.big.screen.service.WaterDevPlanService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/7/28
 */
@RestController
@RequestMapping("waterDevPlan")
public class WaterDevPlanController implements WaterDevPlanControllerApi {

    @Autowired
    WaterDevPlanService waterDevPlanService;

    @GetMapping("statsOfDevPlan")
    @Override
    public CommonResult statsOfDevPlan(String ptId) {
        return waterDevPlanService.statsOfDevPlan(ptId);
    }

    @GetMapping("statsCwindowOfDevPlan")
    @Override
    public CommonResult statsCwindowOfDevPlan(String ptId) {
        return waterDevPlanService.statsCwindowOfDevPlan(ptId);
    }

    @GetMapping("statsOfWaterResearch")
    @Override
    public CommonResult statsOfWaterResearch(Integer year) {
        return waterDevPlanService.statsOfWaterResearch(year);
    }

    @GetMapping("statsCwindowOfWaterResearch")
    @Override
    public CommonResult statsCwindowOfWaterResearch(Integer year) {
        return waterDevPlanService.statsCwindowOfWaterResearch(year);
    }
}
