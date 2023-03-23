package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterProtectionControllerApi;
import com.ygkj.big.screen.model.AttSwhsBase;
import com.ygkj.big.screen.service.WaterProtectionService;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fml
 * @date 2021-04-02 9:26
 * @description 水资源保障专题
 */
@RestController
@RequestMapping("waterProtection")
public class WaterProtectionController implements WaterProtectionControllerApi {

    @Autowired
    private WaterProtectionService waterProtectionService;

    // 水源地统计-行政区划
    @Override
    @GetMapping("/waterSourceAdcdStats")
    public CommonResult<List<WzAdcdInfo>> waterSourceAdcdStats() {

        return waterProtectionService.waterSourceAdcdStats();
    }

    // 水源地统计-类型
    @Override
    @GetMapping("/waterSourceTypeStats")
    public CommonResult<List<DataStatisticsInfo>> waterSourceTypeStats() {

        return waterProtectionService.waterSourceTypeStats();
    }

    // 水源地统计-预警、规模 当前未有

    // 水库超警戒统计-同水事务监管-水库超汛限占比 /resOverLimitStats

    // 水源地列表 - 按水源地类型（0.地下水1.水库2.山塘3.河流）、规模取水量
    @Override
    @GetMapping("/waterSources")
    public CommonResult<List<AttSwhsBase>> waterSources(String type, String scale) {

        return waterProtectionService.waterSources(type, scale);
    }

    // 取水许可有效期内有效取水户数量
    @Override
    @GetMapping("/wiuValidStats")
    public CommonResult<List<DataStatisticsInfo>> wiuValidStats() {

        return waterProtectionService.wiuValidStats();
    }

    // 取水户统计（按行政区划）- 同取水户专题 /adcdWiuStatistics

    // 取水户用水规模（>50万吨,40-50,30-40,20-30,<20）
    @Override
    @GetMapping("/wiuScaleStats")
    public CommonResult<List<DataStatisticsInfo>> wiuScaleStats() {

        return waterProtectionService.wiuScaleStats();
    }

    // 取水曲线（今年、去年取水量对比） -- 按月份
    @Override
    @GetMapping("/wiuUseWaterMonthStats")
    public CommonResult<List<DataStatisticsInfo>> wiuUseWaterMonthStats(String year) {

        return waterProtectionService.wiuUseWaterMonthStats(year);
    }

    // 取水曲线（今年、去年取水量对比） -- 按行政区划
    @Override
    @GetMapping("/wiuUseWaterAdcdStats")
    public CommonResult<List<WzAdcdInfo>> wiuUseWaterAdcdStats(String year) {

        return waterProtectionService.wiuUseWaterAdcdStats(year);
    }

    @GetMapping("patrolInfoProcessStatisticss")
    @Override
    public CommonResult patrolInfoProcessStatisticss() {
        return CommonResult.success(waterProtectionService.patrolInfoProcessStatisticss());
    }

    @PostMapping("happeningListJson")
    @Override
    public CommonResult happeningListJson(Integer pageSize, Integer pageNum, String chaosType, String chaosName, String chaosStatus) {
        return CommonResult.success(waterProtectionService.happeningListJson(pageSize, pageNum, chaosType, chaosName, chaosStatus));
    }

    @GetMapping("patrolInfoPatrolChaosHappStatic")
    @Override
    public CommonResult patrolInfoPatrolChaosHappStatic() {
        return CommonResult.success(waterProtectionService.patrolInfoPatrolChaosHappStatic());
    }

    @GetMapping("patrolChaosHappStaticByAreaCode")
    @Override
    public CommonResult patrolChaosHappStaticByAreaCode() {
        return CommonResult.success(waterProtectionService.patrolChaosHappStaticByAreaCode());
    }

    @GetMapping("patrolChaosHappStaticByChaosType")
    @Override
    public CommonResult patrolChaosHappStaticByChaosType() {
        return CommonResult.success(waterProtectionService.patrolChaosHappStaticByChaosType());
    }

    @GetMapping("patrolChaosHappStaticByChaosStatus")
    @Override
    public CommonResult patrolChaosHappStaticByChaosStatus() {
        return CommonResult.success(waterProtectionService.patrolChaosHappStaticByChaosStatus());
    }
}
