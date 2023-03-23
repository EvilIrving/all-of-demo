package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.model.StPptnR;
import com.ygkj.big.screen.vo.request.FddProjStaStatsReqVo;
import com.ygkj.big.screen.vo.response.ArealRainResVo;
import com.ygkj.big.screen.vo.response.ChartResVo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author xq
 * @Description 新版水旱灾害防御服务层
 * @Date 2021/5/1
 */
public interface FloodDroughtDefenseService {
    /**
     * 天气预报和以前一样
     *
     * @return
     */
    CommonResult weatherForecast();

    /**
     * 气象预报，降水量、风速整合两条谪仙
     *
     * @return
     */
    Object rainWindCurve();

    /**
     * 累计降雨量
     *
     * @return
     */
    CommonResult rainfallStatistic(String areaName, String endTime);

    /**
     * 预报降雨量
     *
     * @return
     */
    CommonResult predictRainfallStatistic();


    /**
     * 降雨分析-累计降雨量
     *
     * @return
     */
    List<ChartResVo<Integer, List<StPptnR>>> rainfallSum(String hours);


    /**
     * 降雨分析-预报降雨量
     *
     * @return
     */
    List<ChartResVo<Integer, List<StPptnR>>> rainfallForecast(String hours);

    /**
     * 降雨分析
     * 单小时时长的累计降雨面雨量
     *
     * @return
     */
    ArealRainResVo areaRainfallSingle(Integer hour);

    /**
     * 降雨分析
     * 单个小时市场预报降雨面雨量
     *
     * @return
     */
    ArealRainResVo areaRainfallForecastSingle(Integer hour);

    /**
     * 内涝统计-积水点
     * 积水点带行政区划
     *
     * @return
     */
    CommonResult waterloggingList();

    /**
     * 内涝统计-内河水位
     * 内河水位带行政区划，且只统计河道水位站
     *
     * @return
     */
    CommonResult innerRiverList();

    /**
     * 水库统计
     * 水库统计-？水位，防洪高水位，纳蓄能力，剩余库容
     *
     * @return
     */
    CommonResult rsvrStats();

    /**
     * 水闸列表
     * 水闸测站的名称，闸上水位，闸下水位，
     *
     * @return
     */
    CommonResult wagaStList();

    /**
     * 工程站点统计
     * 工程站点按流域和行政区划统计，
     *
     * @return
     */
    Object projStaStats(FddProjStaStatsReqVo reqVo);

    /**
     * 潮位站测站列表
     *
     * @return
     */
    CommonResult tideStList();

    /**
     * 堤防灾害保险
     *
     * @return
     */
    CommonResult dikeAndSeawallLengthCount();

    /**
     * 抢险物资和队伍
     *
     * @return
     */
    CommonResult emergencySupportCount();

    /**
     * 统计大中型水库档案当前年 数量 以及列表
     *
     * @return
     */
    CommonResult reservoirArchivesCount();

    /**
     * 监测站点分析 按分类
     *
     * @return
     */
    CommonResult attStBaseCountByType(String type, String stType, Boolean isWrp);

    /**
     * 工程分析
     *
     * @return
     */
    CommonResult projectCount(String type, String stType);

    /**
     * 实时面雨量
     *
     * @return
     */
    CommonResult pastHoursRainfall();

    JSONObject countOverAlarm();

}
