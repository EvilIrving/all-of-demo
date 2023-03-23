package com.ygkj.water.project.controller;

import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;
import com.ygkj.project.PeripheralAnalysisControllerApi;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.PeripheralAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/peripheralAnalysis")
public class PeripheralAnalysisController implements PeripheralAnalysisControllerApi {

    private PeripheralAnalysisService peripheralAnalysisService;

    public PeripheralAnalysisController(@Autowired PeripheralAnalysisService peripheralAnalysisService) {
        this.peripheralAnalysisService = peripheralAnalysisService;
    }

    @PostMapping("")
    @Override
    public CommonResult analysis(ProjectRangeQueryVo queryVo) {
        return CommonResult.success(peripheralAnalysisService.analysis(queryVo));
    }

    @PostMapping("/records")
    @Override
    public CommonResult records(String codes, String type) {
        return CommonResult.success(peripheralAnalysisService.candidateRecords(codes, type));
    }
}
