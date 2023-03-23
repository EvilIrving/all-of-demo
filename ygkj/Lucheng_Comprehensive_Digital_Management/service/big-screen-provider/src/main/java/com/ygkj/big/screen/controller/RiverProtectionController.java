package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.RiverProtectionControllerApi;
import com.ygkj.big.screen.service.RiverProtectionService;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fml
 * @date 2021-03-22 16:02
 * @description 河湖保护专题
 */
@RestController
@RequestMapping("/riverProtection")
public class RiverProtectionController implements RiverProtectionControllerApi {

    @Autowired
    private RiverProtectionService riverProtectionService;

    // 获取温州行政区划信息
    @Override
    @GetMapping("/getWzAdcds")
    public List<WzAdcdInfo> getWzAdcds() {

        return riverProtectionService.getWzAdcds();
    }

    // 河流数量统计
    @Override
    @GetMapping("/riverNumStatistics")
    public CommonResult<List<RpRiverNumStatitstcsRespVo>> riverNumStatistics(String adcd) {

        return riverProtectionService.riverNumStatistics(adcd);
    }

    // 河道水位列表（实时水位判断是否预警）
    @Override
    @GetMapping("/waterLevels")
    public CommonResult<List<RpWaterLevelsRespVo>> waterLevels(String adcd) {

        return riverProtectionService.waterLevels(adcd);
    }

    // 四乱问题统计(行政区划、类型、处理状态)
    @Override
    @GetMapping("/problemStatistics")
    public CommonResult<List<RpProblemStatsRespVo>> problemStatistics(String type) {

        return riverProtectionService.problemStatistics(type);
    }

    // 雨量分析（1流域，2行政区划）
    @Override
    @GetMapping("/railfallStatistics")
    public CommonResult<List<RpRainfallRespVo>> railfallStatistics(String adcd, String type) {

        return riverProtectionService.railfallStatistics(adcd, type);
    }

    // 地图点位(1水源地、2水库、 3水质监测、 4水位监测、5雨量监测、 6河湖全景图、 7摄像机、 8四乱点位)
    @Override
    @GetMapping("/mapPoint")
    public CommonResult<RpMapPointsRespVo> mapPoint(String type, String id) {

        return riverProtectionService.mapPoint(type, id);
    }

    // 水位统计
    @Override
    @GetMapping("/waterLevelStatistics")
    public CommonResult<List<RpWarnNumStatsRespVo>> waterLevelStatistics() {

        return riverProtectionService.waterLevelStatistics();
    }

    // 水质统计
    @Override
    @GetMapping("/szStatistics")
    public CommonResult<List<RpWarnNumStatsRespVo>> szStatistics() {

        return riverProtectionService.szStatistics();
    }

    // 雨量统计
    @Override
    @GetMapping("/rainfallStatistics")
    public CommonResult<List<RpWarnNumStatsRespVo>> rainfallStatistics() {

        return riverProtectionService.rainfallStatistics();
    }

    // 视频统计
    @Override
    @GetMapping("/cameraStatistics")
    public CommonResult<List<RpWarnNumStatsRespVo>> cameraStatistics() {

        return riverProtectionService.cameraStatistics();
    }

    // 行政区划内设备统计（1水位/2水质/3雨量/4视频）
    @Override
    @GetMapping("/adcdEquipmentStats")
    public CommonResult<List<WzAdcdInfo>> adcdEquipmentStats(String type) {

        return riverProtectionService.adcdEquipmentStats(type);
    }

    // 行政区划河湖库数量统计（1河流/2湖泊/3水位）
    @Override
    @GetMapping("/adcdRLRStats")
    public CommonResult<List<WzAdcdInfo>> adcdRLRStats(String type) {

        return riverProtectionService.adcdRLRStats(type);
    }

    // 水域变化面积、河长巡查率、批后监管（不清楚）

}
