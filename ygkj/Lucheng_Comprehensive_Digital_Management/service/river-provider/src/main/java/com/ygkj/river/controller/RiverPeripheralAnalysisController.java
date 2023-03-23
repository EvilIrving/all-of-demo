package com.ygkj.river.controller;

import com.ygkj.river.RiverPeripheralAnalysisControllerApi;
import com.ygkj.river.service.RiverPeripheralAnalysisService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/riverPeripheralAnalysis")
public class RiverPeripheralAnalysisController implements RiverPeripheralAnalysisControllerApi {

    private RiverPeripheralAnalysisService riverPeripheralAnalysisService;

    public RiverPeripheralAnalysisController(@Autowired RiverPeripheralAnalysisService riverPeripheralAnalysisService) {
        this.riverPeripheralAnalysisService = riverPeripheralAnalysisService;
    }

    @PostMapping("")
    @Override
    public CommonResult peripheralAnalysisTree(Integer riverSegId, Integer distance) {
        return CommonResult.success(riverPeripheralAnalysisService.peripheralAnalysisTree(riverSegId, distance));
    }

    @PostMapping("/selectPoints")
    @Override
    public CommonResult selectPoints(String type, Integer riverSegId, Integer distance) {
        return CommonResult.success(riverPeripheralAnalysisService.selectPoints(type, riverSegId, distance));
    }
}
