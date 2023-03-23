package com.ygkj.gragh.controller;

import com.ygkj.gragh.RiverProtectionControllerApi;
import com.ygkj.gragh.service.LineService;
import com.ygkj.gragh.service.ProjectService;
import com.ygkj.gragh.service.RiverService;
import com.ygkj.gragh.service.WaterAreaService;
import com.ygkj.gragh.vo.request.LineQueryVo;
import com.ygkj.gragh.vo.request.RiverProjectQueryVo;
import com.ygkj.gragh.vo.request.RiverQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riverProtection")
public class RiverProtectionController implements RiverProtectionControllerApi {

    @Autowired
    private RiverService riverService;
    @Autowired
    private LineService lineService;
    @Autowired
    private WaterAreaService waterAreaService;
    @Autowired
    private ProjectService projectService;


    @PostMapping("/rivers")
    @Override
    public CommonResult rivers(RiverQueryVo riverQueryVo) {
        return CommonResult.success(riverService.rivers(riverQueryVo));
    }

    @PostMapping("/lines")
    @Override
    public CommonResult lines(LineQueryVo lineQueryVo) {
        return CommonResult.success(lineService.lines(lineQueryVo));
    }


    @PostMapping("/countYearlyArea")
    @Override
    public CommonResult countYearlyArea() {
        return CommonResult.success(riverService.countYearlyArea());
    }

    @GetMapping("/waterAreas")
    @Override
    public CommonResult waterAreas() {
        return CommonResult.success(waterAreaService.waterAreas());
    }

    @PostMapping("/riverProjects")
    @Override
    public CommonResult riverProjects(RiverProjectQueryVo riverProjectQueryVo) {
        return CommonResult.success(projectService.riverProjects(riverProjectQueryVo));
    }
}
