package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterDisasterControllerApi;
import com.ygkj.big.screen.model.RealTyphoonData;
import com.ygkj.big.screen.service.WaterDisasterService;
import com.ygkj.big.screen.vo.response.WaResStaWaterLevelInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author fml
 * @date 2021-03-30 19:12
 * @description 水灾害防御专题
 */
public class WaterDisasterController implements WaterDisasterControllerApi {

    @Autowired
    private WaterDisasterService waterDisasterService;

    // 雨量预警（1小时降雨量超警数量、3小时降雨量超警数量）
    @Override
    @GetMapping("/rainfallWarn")
    public CommonResult<List<WuWiuStatsRespVo>> rainfallWarn(Integer hour) {

        return waterDisasterService.rainfallWarn(hour);
    }

    // 雨量预警（1小时、3小时量）统计不同降雨量区间，雨量站的数量 ＜10、10-25、25-50、50-100、100-250、＞250
    @Override
    @GetMapping("/rainfallInteravalStats")
    public CommonResult<List<WuWiuStatsRespVo>> rainfallInteravalStats(Integer hour) {

        return waterDisasterService.rainfallInteravalStats(hour);
    }

    // 水库纳蓄量（大中型水库累加总库容-大中型水库累加实时库容）
    @Override
    @GetMapping("/largeResCap")
    public CommonResult<List<WuWiuStatsRespVo>> largeResCap() {

        return waterDisasterService.largeResCap();
    }

    // 水库纳蓄能力（纳蓄能力前五，纳蓄能力=（汛限水库对应库容-实时水位对应库容）/集水面积）
    @Override
    @GetMapping("/resCapAbility")
    public CommonResult<List<WaResStaWaterLevelInfo>> resCapAbility() {

        return waterDisasterService.resCapAbility();
    }

    // 水库超汛限-同水事务监管水库超汛限 /resOverLimitStats

    // 河道超警戒-同河湖保护专题河道水位统计 /waterLevelStatistics

    // 台风等级统计(≤8级、9~10级、11~12级、13~14级、＞14级)
    @Override
    @GetMapping("/typhoonPowerStats")
    public CommonResult<List<WuWiuStatsRespVo>> typhoonPowerStats(Integer year) {

        return waterDisasterService.typhoonPowerStats(year);
    }

    // 历史台风数量、历史最大台风等级
    @Override
    @GetMapping("/hisTyphoonStats")
    public CommonResult<List<WuWiuStatsRespVo>> hisTyphoonStats(Integer year, String type) {

        return waterDisasterService.hisTyphoonStats(year, type);
    }

    // 统计不同年份台风数量
    @Override
    @GetMapping("/yearTyphoonStats")
    public CommonResult<List<WuWiuStatsRespVo>> yearTyphoonStats(Integer year) {

        return waterDisasterService.yearTyphoonStats(year);
    }

    // 仓库统计-同防汛抢险专题 /wareHouseStats
    // 队伍统计-同防汛抢险专题 /teamStats

    // 地图--历史台风信息
    @Override
    @GetMapping("/typhoonInfos")
    public CommonResult<List<RealTyphoonData>> typhoonInfos(String id, String year, String landPower) {

        return waterDisasterService.typhoonInfos(id, year, landPower);
    }

}
