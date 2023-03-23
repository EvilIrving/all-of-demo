package com.ygkj.big.screen.service;

import com.ygkj.big.screen.vo.response.*;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * 取水户专题
 */
public interface WaterUserService {

    /**
     * 取水户点位
     *
     * @param type   1工业取水、2水利发电、3公共供、4农业取水、5其他
     * @param status 状态 1正常、2许可证过期、3水量超许可、4掉线或未安装监控设施、5取水偏快提醒
     * @return CommonResult
     */
    CommonResult<List<WuWaterUserMapRespVo>> mapPoints(String type, String status);

    /**
     * 取水户月取水量弹框
     *
     * @param wiuCode 取水户编码
     * @return CommonResult
     */
    CommonResult<WuWaterUseRespVo> waterUserUseMonth(String wiuCode);

    /**
     * 取水许可总量统计（按行政区划区分）
     *
     * @return
     */
    CommonResult<List<WzAdcdInfo>> adcdWptStatistics();

    /**
     * 取水许可类型统计（行政区划下取水类型）
     *
     * @param adcd 行政区划
     * @return CommonResult
     */
    CommonResult<List<WuWptQslxStatsRespVo>> wptQslxStatistics(String adcd);

    /**
     * 取水户统计（按行政区划区分）
     *
     * @return CommonResult
     */
    CommonResult<List<WzAdcdInfo>> adcdWiuStatistics();

    /**
     * 取水曲线（一年内月取水曲线）
     *
     * @param year 年份
     * @return CommonResult
     */
    CommonResult<List<WuWiuUseCurveRespVo>> wiuUseCurve(String year);

    /**
     * 档案完整率
     *
     * @return CommonResult
     */
    CommonResult wiuDocRate();

    /**
     * 取水预警统计（许可量80%预警）
     *
     * @return CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> waterUseWarn();
}
