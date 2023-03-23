package com.ygkj.big.screen.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.HydrologyAnalysisApi;
import com.ygkj.big.screen.service.HydrologyAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/24
 */
@RestController
@RequestMapping("hydrologyAnalysis")
public class HydrologyAnalysisController implements HydrologyAnalysisApi {

    @Autowired
    HydrologyAnalysisService hydrologyAnalysisService;

    @GetMapping("warnStatistic")
    @Override
    public List<JSONObject> warnStatistic() {
        return hydrologyAnalysisService.warnStatistic();
    }

    @GetMapping("tideExtreme")
    @Override
    public List<JSONObject> tideExtreme(@RequestParam("type") Integer type) {
        return hydrologyAnalysisService.tideExtreme(type);
    }

    @GetMapping("waterLevelAnalysis")
    @Override
    public List<JSONObject> waterLevelAnalysis(@RequestParam("type") Integer type) {
        return hydrologyAnalysisService.waterLevelAnalysis(type);
    }

    @GetMapping("rainfallExtreme")
    @Override
    public List<JSONObject> rainfallExtreme() {
        return hydrologyAnalysisService.rainfallExtreme();
    }

    @GetMapping("waterLevelExtreme")
    @Override
    public List<JSONObject> waterLevelExtreme() {
        return hydrologyAnalysisService.waterLevelExtreme();
    }
}
