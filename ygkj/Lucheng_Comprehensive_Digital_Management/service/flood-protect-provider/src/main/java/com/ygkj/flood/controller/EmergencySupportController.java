package com.ygkj.flood.controller;

import com.ygkj.flood.EmergencySupportControllerApi;
import com.ygkj.flood.service.EmergencySupportService;
import com.ygkj.flood.service.WaterLevelAnalyseService;
import com.ygkj.flood.vo.request.EmergencyDispatchRequestVo;
import com.ygkj.flood.vo.request.IntelligentAnalysisVo;
import com.ygkj.flood.vo.request.SynRainRequestVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergencySupport")
public class EmergencySupportController implements EmergencySupportControllerApi {

    @Autowired
    private WaterLevelAnalyseService waterLevelAnalyseService;

    @Autowired
    private EmergencySupportService emergencySupportService;

    @GetMapping("/selectTodayWarningStation")
    @Override
    public CommonResult selectTodayWarningStation() {
        return CommonResult.success(waterLevelAnalyseService.selectTodayWarningStation());
    }

    @PostMapping("/emergencyDispatch")
    @Override
    public CommonResult emergencyDispatch(EmergencyDispatchRequestVo requestVo) {
        return CommonResult.success(emergencySupportService.emergencyDispatch(requestVo));
    }

    @GetMapping("/materials")
    @Override
    public CommonResult materials() {
        return CommonResult.success(emergencySupportService.materials());
    }

    @PostMapping("/intelligentAnalysis")
    @Override
    public CommonResult intelligentAnalysis(@RequestBody IntelligentAnalysisVo intelligentAnalysisVo) {
        return CommonResult.success(emergencySupportService.intelligentAnalysis(intelligentAnalysisVo.getTeamId()
                , intelligentAnalysisVo.getList()));
    }

    @PostMapping("/rainfallWarningInfo")
    @Override
    public CommonResult rainfallWarningInfo(SynRainRequestVo synRainRequestVo) {
        return CommonResult.success(emergencySupportService.rainfallWarningInfo(synRainRequestVo));
    }

    @GetMapping("/hazardPoints")
    @Override
    public CommonResult hazardPoints() {
        return CommonResult.success(emergencySupportService.hazardPoints());
    }

    @GetMapping("/emergencySupportCount")
    @Override
    public CommonResult emergencySupportCount() {
        return CommonResult.success(emergencySupportService.emergencySupportCount());
    }
}
