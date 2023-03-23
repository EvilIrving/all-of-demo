package com.ygkj.flood.controller;

import com.ygkj.flood.WaterLevelAnalyseControllerApi;
import com.ygkj.flood.service.WaterLevelAnalyseService;
import com.ygkj.flood.vo.response.WlaFloatWinWaterLevelRespVo;
import com.ygkj.flood.vo.response.WlaHisHighestWaterLevelRespVo;
import com.ygkj.flood.vo.response.WlaOverWarnNumRespVo;
import com.ygkj.flood.vo.response.WlaWarnNumStatisticsRespVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/waterLevelAnalyse")
public class WaterLevelAnalyseController implements WaterLevelAnalyseControllerApi {

    @Autowired
    private WaterLevelAnalyseService waterLevelAnalyseService;

    // 水位历史极值（RR水库、ZZ河道、DD闸坝）
    @Override
    @GetMapping("/hisHighestWaterLevel")
    public CommonResult<List<WlaHisHighestWaterLevelRespVo>> hisHighestWaterLevel(String type, String stName, String stCode) {

        return waterLevelAnalyseService.hisHighestWaterLevel(type, stName, stCode);
    }

    // 水库、河道、闸坝报警数
    @Override
    @GetMapping("/warnNumStatistics")
    public CommonResult<List<WlaWarnNumStatisticsRespVo>> warnNumStatistics() {

        return waterLevelAnalyseService.warnNumStatistics();
    }

    // 超警、超保、超汛限数量统计
    @Override
    @GetMapping("/overWarnStatistics")
    public CommonResult<WlaOverWarnNumRespVo> overWarnStatistics() {

        return waterLevelAnalyseService.overWarnStatistics();
    }

    // 浮窗-大中型水库（实时水位、汛限水位、防洪高水位）、河道/闸坝（实时水位、警戒水位、保证水位）、潮位（实时潮位、蓝色警戒潮位）
    @Override
    @GetMapping("/floatWinWaterLevel")
    public CommonResult<List<WlaFloatWinWaterLevelRespVo>> floatWinWaterLevel(String type, String name, String adcd, String stCode) {
        return waterLevelAnalyseService.floatWinWaterLevel(type, name, adcd, stCode);
    }

    // 流域水位和平原水位是河道、闸坝中的一部分（实时水位、警戒水位、保证水位）


    @GetMapping("/getAllStWatLev")
    @Override
    public CommonResult getAllStWatLev(@RequestParam(value = "stName", required = false) String stName) {
        return CommonResult.success(waterLevelAnalyseService.getAllStWatLev(stName));
    }

    @GetMapping("getHistoricWaterLevelExtremum")
    @Override
    public CommonResult getHistoricWaterLevelExtremum(@RequestParam(value = "stName", required = false) String stName) {
        return CommonResult.success(waterLevelAnalyseService.getHistoricWaterLevelExtremum(stName));
    }

    @GetMapping("watLevOfPlainBasInnerRiv")
    @Override
    public CommonResult watLevOfPlainBasInnerRiv(@RequestParam(value = "stName", required = false) String stName,
                                                 @RequestParam(value = "type") Integer type) {
        return CommonResult.success(waterLevelAnalyseService.watLevOfPlainBasInnerRiv(stName, type));
    }

    @GetMapping("innerRiverWaterLevel")
    @Override
    public CommonResult innerRiverWaterLevel(@RequestParam(value = "stName", required = false) String stName) {
        return CommonResult.success(waterLevelAnalyseService.innerRiverWaterLevel(stName));
    }
}
