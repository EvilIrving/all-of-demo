package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterUserControllerApi;
import com.ygkj.big.screen.service.WaterUserService;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 取水户专题
 */
@RestController
@RequestMapping("/waterUser")
public class WaterUserController implements WaterUserControllerApi {

    @Autowired
    private WaterUserService waterUserService;

    // 取水户点位(1工业取水、2水利发电、3公共供、4农业取水、5其他)
    // 状态 1正常、2许可证过期、3水量超许可、4掉线或未安装监控设施、5取水偏快提醒
    @Override
    @GetMapping("/mapPoints")
    public CommonResult<List<WuWaterUserMapRespVo>> mapPoints(String type, String status) {

        return waterUserService.mapPoints(type, status);
    }

    // 取水户月取水量弹框
    @Override
    @GetMapping("/waterUserUseMonth")
    public CommonResult<WuWaterUseRespVo> waterUserUseMonth(String wiuCode) {

        return waterUserService.waterUserUseMonth(wiuCode);
    }

    // 取水许可总量统计（按行政区划区分）
    @Override
    @GetMapping("/adcdWptStatistics")
    public CommonResult<List<WzAdcdInfo>> adcdWptStatistics() {

        return waterUserService.adcdWptStatistics();
    }

    // 取水许可类型统计（行政区划下取水类型）
    @Override
    @GetMapping("/wptQslxStatistics")
    public CommonResult<List<WuWptQslxStatsRespVo>> wptQslxStatistics(String adcd) {

        return waterUserService.wptQslxStatistics(adcd);
    }

    // 取水户统计（按行政区划区分）
    @Override
    @GetMapping("/adcdWiuStatistics")
    public CommonResult<List<WzAdcdInfo>> adcdWiuStatistics() {

        return waterUserService.adcdWiuStatistics();
    }

    // 取水曲线（过去一年月取水曲线）
    @Override
    @GetMapping("/wiuUseCurve")
    public CommonResult<List<WuWiuUseCurveRespVo>> wiuUseCurve(String year) {

        return waterUserService.wiuUseCurve(year);
    }

    // 档案完整率(（取水户数量-档案必须项确实的数量）/取水户数量)
    @Override
    @GetMapping("/wiuDocRate")
    public CommonResult wiuDocRate() {

        return waterUserService.wiuDocRate();
    }

    // 取水预警统计（许可量80%预警）
    @Override
    @GetMapping("/waterUseWarn")
    public CommonResult<List<WuWiuStatsRespVo>> waterUseWarn() {

        return waterUserService.waterUseWarn();
    }

}
