package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.FddProjStaStatsReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/5/1
 */
@Api(tags = "新版-水旱灾害防御")
public interface FloodDroughtDefenseControllerApi {
    /**
     * 天气预报和以前一样
     *
     * @return
     */
    @ApiOperation("天气预报")
    CommonResult weatherForecast();

    /**
     * 气象预报，降水量、风速整合两条谪仙
     *
     * @return
     */
    @ApiOperation("气象预报")
    CommonResult rainWindCurve();

    @ApiOperation("累计降雨量")
    CommonResult rainfall(String areaName, String endTime, String drillAffairId);

    @ApiOperation("预报降雨量")
    CommonResult predictRainfallStatistic();

    /**
     * 降雨分析
     * 多个小时时长的累计降雨量
     *
     * @return
     */
    @ApiOperation("降雨分析-累计降雨量")
    CommonResult rainfallSum(String hours);

    /**
     * 降雨分析
     * 多个小时市场的预报降雨量
     *
     * @return
     */
    @ApiOperation("降雨分析-预报降雨量")
    CommonResult rainfallForecast(String hours);

    /**
     * 降雨分析
     * 单小时时长的累计降雨面雨量
     *
     * @return
     */
    @ApiOperation("降雨分析-单小时累计降雨量")
    CommonResult areaRainfallSingle(Integer hour);

    /**
     * 降雨分析
     * 单个小时市场预报降雨面雨量
     *
     * @return
     */
    @ApiOperation("降雨分析-单小时预报降雨量")
    CommonResult areaRainfallForecastSingle(Integer hour);

    /**
     * 内涝统计-积水点
     * 积水点带行政区划
     *
     * @return
     */
    @ApiOperation("内涝统计-积水点")
    CommonResult waterloggingList();

    /**
     * 内涝统计-内河水位
     * 内河水位带行政区划，且只统计河道水位站
     *
     * @return
     */
    @ApiOperation("内涝统计-内河水位")
    CommonResult innerRiverList();

    /**
     * 水库统计
     * 水库统计-？水位，防洪高水位，纳蓄能力，剩余库容
     *
     * @return
     */
    @ApiOperation("水库统计")
    CommonResult rsvrStats();

    /**
     * 水闸列表
     * 水闸测站的名称，闸上水位，闸下水位，
     *
     * @return
     */
    @ApiOperation("水闸列表")
    CommonResult wagaStList();

    /**
     * 工程站点统计
     * 工程站点按流域和行政区划统计，
     *
     * @return
     */
    @ApiOperation("工程站点统计")
    CommonResult projStaStats(FddProjStaStatsReqVo reqVo);

    /**
     * 潮位站测站列表
     *
     * @return
     */
    @ApiOperation("潮位站测站列表")
    CommonResult tideStList();

    @ApiOperation("堤防灾害保险")
    CommonResult dikeAndSeawallLengthCount();

    @ApiOperation("抢险物资和队伍")
    CommonResult emergencySupportCount();

    @ApiOperation("工程分析")
    CommonResult projectCount(String type, String stType);

    @ApiOperation("实时面雨量")
    CommonResult pastHoursRainfall();

    @ApiOperation("统计大中型水库档案当前年 数量 以及列表")
    CommonResult reservoirArchivesCount();


    @ApiOperation("监测站分类 统计   type 有 行政区域  流域  工程类型  站点类型")
    CommonResult attStBaseCountByType(String type, String stType, Boolean isWrp);

    @ApiOperation("app统计超警数量")
    CommonResult countOverAlarm();
}

