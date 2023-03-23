package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.FloodRescueControllerApi;
import com.ygkj.big.screen.service.FloodRescueService;
import com.ygkj.big.screen.vo.response.FrWaterLevelStaRespVo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 防汛抢险专题
 */
@RestController
@RequestMapping("/floodRescue")
public class FloodRescueController implements FloodRescueControllerApi {

    @Autowired
    private FloodRescueService floodRescueService;

    // 十大工程点位（水库、山塘、提防、海塘、水电站、泵站、水闸、闸站、灌区、农饮水）积水点、内河水位点、仓库、队伍以及摄像机
    @Override
    @GetMapping("/mapPoints")
    public CommonResult mapPoints(String type, String id) {

        return floodRescueService.mapPoints(type, id);
    }

    // 仓库数量统计（按行政区划）att_warehouse_base
    @Override
    @GetMapping("/wareHouseStats")
    public CommonResult<List<WzAdcdInfo>> wareHouseStats() {

        return floodRescueService.wareHouseStats();
    }

    // 队伍数量统计（按行政区划）att_team_base
    @Override
    @GetMapping("/teamStats")
    public CommonResult<List<WzAdcdInfo>> teamStats() {

        return floodRescueService.teamStats();
    }

    // 人员数量统计（队伍人员数量统计，按行政区划）
    @Override
    @GetMapping("/teamPersonStats")
    public CommonResult<List<WzAdcdInfo>> teamPersonStats() {

        return floodRescueService.teamPersonStats();
    }

    // 水利工程统计（按区划、按工程类型）
    @Override
    @GetMapping("/projStats")
    public CommonResult<List<WzAdcdInfo>> projStats(String type) {

        return floodRescueService.projStats(type);
    }

    // 工情轮播--水闸 synchronizedStatistic/synchronizedGate  waterAndRain/waterStations
    // 工情轮播--水泵 synchronizedStatistic/synchronizedGate
    // 工情轮播--水库 synchronizedStatistic/synchronizedGate
    @Override
    @GetMapping("/projConditions")
    public CommonResult projConditions(String type) {

        return floodRescueService.projConditions(type);
    }

    // 水位轮播（水库+河道）
    @Override
    @GetMapping("/waterLevels")
    public CommonResult<List<FrWaterLevelStaRespVo>> waterLevels() {

        return floodRescueService.waterLevels();
    }

    // 视频轮播(视频点位列表)河湖专题中
}
