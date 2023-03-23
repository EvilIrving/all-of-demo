package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterAffairsControllerApi;
import com.ygkj.big.screen.model.HikvisionCameraRecord;
import com.ygkj.big.screen.model.WtSoilErosionProjBase;
import com.ygkj.big.screen.service.WaterAffairsService;
import com.ygkj.big.screen.vo.response.WaSoilErosionRespVo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fml
 * @date 2021-03-29 10:48
 * @description 水事务监管专题
 */
@RestController
@RequestMapping("/affairs")
public class WaterAffairsController implements WaterAffairsControllerApi {

    @Autowired
    private WaterAffairsService waterAffairsService;

    // 地图点位 十大工程、视频、水土流失图斑、治理工程图斑（利用图层切换不同的地图点位）
    // 水土流失图斑 (遥感影像)
    @Override
    @GetMapping("/soilEroMapPoints")
    public CommonResult<List<WaSoilErosionRespVo>> soilEroMapPoints() {

        return waterAffairsService.soilEroMapPoints();
    }

    // 治理工程图斑 (工程边界)
    @Override
    @GetMapping("/projMapPoints")
    public CommonResult<List<WtSoilErosionProjBase>> projMapPoints(String id) {

        return waterAffairsService.projMapPoints(id);
    }

    // 工程统计--同防汛

    // 摄像头统计-总数统计（按区划、按工程类型）
    // 摄像头统计-异常数数统计（按区划、按工程类型）
    @Override
    @GetMapping("/cameraStats")
    public CommonResult<List<WzAdcdInfo>> cameraStats(String warnStatus, String stats) {

        return waterAffairsService.cameraStats(warnStatus, stats);
    }

    // 地图图例--摄像机正异常点位(1正常，2异常)
    @Override
    @GetMapping("/cameraPoints")
    public CommonResult<List<HikvisionCameraRecord>> cameraPoints(String status) {

        return waterAffairsService.cameraPoints(status);
    }

    // 水库超汛限占比
    @Override
    @GetMapping("/resOverLimitStats")
    public CommonResult<List<WuWiuStatsRespVo>> resOverLimitStats() {

        return waterAffairsService.resOverLimitStats();
    }

    // 各区县水库平均蓄水率=实时库容/总库容
    @Override
    @GetMapping("/waterStorageStats")
    public CommonResult<List<WzAdcdInfo>> waterStorageStats() {

        return waterAffairsService.waterStorageStats();
    }

    // 水土流失面积统计（1按年份,2按行政区划）
    @Override
    @GetMapping("/soilErosionStats")
    public CommonResult soilErosionStats(String type) {

        return waterAffairsService.soilErosionStats(type);
    }

    // 水土流失-治理图斑统计-治理面积（1按年份,2按行政区划）
    @Override
    @GetMapping("/soilErosionGovernStats")
    public CommonResult soilErosionGovernStats(String type) {

        return waterAffairsService.soilErosionGovernStats(type);
    }

    // 水土流失-治理项目统计（1按年份,2按行政区划）
    @Override
    @GetMapping("/soilErosionProjStats")
    public CommonResult soilErosionProjStats(String type) {

        return waterAffairsService.soilErosionProjStats(type);
    }

    @Override
    @GetMapping("/supplyStats")
    public CommonResult waterSupplyStats(@RequestParam("type") int type) {
        return waterAffairsService.waterSupplyStats(type);
    }

//    /**
//     * 水厂运行状态统计
//     * @param result  1按数量 。2按人口
//     * @param data 1统计 2柱状图
//     * @return
//     */
//    @Override
//    @GetMapping("/operateStats")
//    public CommonResult waterOperateStats(String result, String data) {
//
//        return CommonResult.success(waterAffairsService.waterOperateStats(result,data));
//    }
}
