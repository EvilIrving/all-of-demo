package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.model.AttSwhsBase;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author fml
 * @date 2021-04-02 9:39
 * @description 水资源保障专题
 */
public interface WaterProtectionService {

    /**
     * 水源地统计-行政区划
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WzAdcdInfo>> waterSourceAdcdStats();

    /**
     * 水源地统计-类型
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> waterSourceTypeStats();

    /**
     * 水源地列表 - 按水源地类型、规模取水量
     *
     * @param type  按水源地类型 0.地下水1.水库2.山塘3.河流
     * @param scale 暂无
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<AttSwhsBase>> waterSources(String type, String scale);

    /**
     * 取水许可有效期内有效取水户数量
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> wiuValidStats();

    /**
     * 取水户用水规模（>50万吨,40-50,30-40,20-30,<20）
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> wiuScaleStats();

    /**
     * 取水曲线（今年、去年取水量对比） -- 按月份
     *
     * @param year 年份
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> wiuUseWaterMonthStats(String year);

    /**
     * 取水曲线（今年、去年取水量对比） -- 按行政区划
     *
     * @param year 年份
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WzAdcdInfo>> wiuUseWaterAdcdStats(String year);

    /**
     * 数字珊溪C位统计
     *
     * @return
     */
    JSONObject patrolInfoProcessStatisticss();

    /**
     * 巡查违法列表
     *
     * @param pageSize
     * @param pageNum
     * @param chaosType
     * @param chaosName
     * @param chaosStatus
     * @return
     */
    List<JSONObject> happeningListJson(Integer pageSize, Integer pageNum, String chaosType, String chaosName, String chaosStatus);

    /**
     * 巡查数量统计
     *
     * @return
     */
    JSONObject patrolInfoPatrolChaosHappStatic();

    /**
     * 巡查按行政区划统计
     *
     * @return
     */
    JSONArray patrolChaosHappStaticByAreaCode();

    /**
     * 巡查乱点分析统计
     *
     * @return
     */
    JSONArray patrolChaosHappStaticByChaosType();

    /**
     * 巡查处置状态统计
     *
     * @return
     */
    JSONArray patrolChaosHappStaticByChaosStatus();

}
