package com.ygkj.flood.controller;

import com.ygkj.flood.RainfallAnalysisControllerApi;
import com.ygkj.flood.service.RainfallAnalysisService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description 防汛形势研判-降雨分析
 * @Date 2021/1/18
 */
@RestController
@RequestMapping("rainfallAnalysis")
public class RainfallAnalysisController implements RainfallAnalysisControllerApi {

    @Autowired
    RainfallAnalysisService rainfallAnalysisService;

    @GetMapping("/getAllAdcdRealtimeRainfall")
    @Override
    public CommonResult getAllAdcdRealtimeRainfall(@RequestParam("minutes") Integer minutes, @RequestParam(value = "adnm", required = false) String adnm) {
        return CommonResult.success(rainfallAnalysisService.getAllAdcdRealtimeRainfall(minutes, adnm));
    }

    @GetMapping("/getAllBasinRealtimeRainfall")
    @Override
    public CommonResult getAllBasinRealtimeRainfall(@RequestParam("minutes") Integer minutes,
                                                    @RequestParam(value = "basin", required = false) String basin) {
        return CommonResult.success(rainfallAnalysisService.getAllBasinRealtimeRainfall(minutes, basin));
    }

//    @GetMapping("/getRealtimeRainLevelStatistic")
//    @Override
//    public CommonResult getRealtimeRainLevelStatistic(Integer minutes) {
//        return CommonResult.success(rainfallAnalysisService.getRealtimeRainLevelStatistic(minutes));
//    }
//
//    /**
//     * 2021-01-25
//     * 按宋亚路建议，历史极值方案还未确定，目前只需要查询历史极值就行了，不需要做和实时雨量做额外比较
//     *
//     */
//    @GetMapping("/getRainfallExtremum")
//    @Override
//    public CommonResult getRainfallExtremum(@RequestParam(name = "minutes",required = false) Integer minutes,
//                                            @RequestParam(name = "stName",required = false) String stName) {
//        return CommonResult.success(rainfallAnalysisService.getRainfallExtremum(minutes,stName));
//    }

    @GetMapping("/getRainWarningStationNum")
    @Override
    public CommonResult getRainWarningStationNum(Integer hours) {
        return CommonResult.success(rainfallAnalysisService.getRainWarningStationNum(hours));
    }

    @GetMapping("getHistoricRainfallExtremum")
    @Override
    public CommonResult getHistoricRainfallExtremum() {
        return CommonResult.success(rainfallAnalysisService.getHistoricRainfallExtremum());
    }

    @GetMapping("getPpStNumStatistic")
    @Override
    public CommonResult getPpStNumStatistic(@RequestParam("minutes") Integer minutes) {
        return CommonResult.success(rainfallAnalysisService.getPpStNumStatistic(minutes));
    }

    @GetMapping("getCollectPointRainfall")
    @Override
    public CommonResult getCollectPointRainfall() {
        return CommonResult.success(rainfallAnalysisService.getCollectPointRainfall());
    }

    @GetMapping("getResPointRainfall")
    @Override
    public CommonResult getResPointRainfall() {
        return CommonResult.success(rainfallAnalysisService.getResStRainfall());
    }


}
