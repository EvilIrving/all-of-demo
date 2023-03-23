package com.ygkj.gragh.controller;

import com.ygkj.gragh.WaterPlanControllerApi;
import com.ygkj.gragh.service.WaterPlanService;
import com.ygkj.gragh.vo.request.ProjectReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("water-plan")
public class WaterPlanController implements WaterPlanControllerApi {

    @Resource
    private WaterPlanService waterPlanService;

    @Override
    @PostMapping("list-project")
    public CommonResult listProject(ProjectReqVo projectReqVo) {
        return CommonResult.success(waterPlanService.listProject(projectReqVo));
    }

    @Override
    @GetMapping("select-build")
    public CommonResult selectBuild(String id) {
        return CommonResult.success(waterPlanService.selectBuild(id));
    }

    @Override
    @GetMapping("select-programme")
    public CommonResult selectProgramme(String id) {
        return CommonResult.success(waterPlanService.selectProgramme(id));
    }

    @Override
    @GetMapping("select-plan")
    public CommonResult selectPlan(String id) {
        return CommonResult.success(waterPlanService.selectPlan(id));
    }

    @Override
    @GetMapping("select-plot")
    public CommonResult selectPlot(String id) {
        return CommonResult.success(waterPlanService.selectPlot(id));
    }

    @Override
    @GetMapping("select-early")
    public CommonResult selectEarly(String id) {
        return CommonResult.success(waterPlanService.selectEarly(id));
    }
}
