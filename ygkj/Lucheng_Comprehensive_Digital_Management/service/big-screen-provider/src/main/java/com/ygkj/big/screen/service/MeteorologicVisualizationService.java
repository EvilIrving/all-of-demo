package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/20
 */
public interface MeteorologicVisualizationService {

    /**
     * 天气预报
     *
     * @return
     */
    List<JSONObject> forecast();

    /**
     * 气象统计
     *
     * @return
     */
    List<JSONObject> meteorologicStatistic(String type);

    /**
     * 台风统计
     *
     * @return
     */
    List<JSONObject> typhoonStatistic();

    /**
     * 最近台风
     *
     * @return
     */
    List<JSONObject> typhoonRecent();

    /**
     * 相似台风
     *
     * @return
     */
    List<JSONObject> similarTyphoon(String type);

    /**
     * 实时降雨
     *
     * @return
     */
    List<JSONObject> realtimeRainfall();

    /**
     * 预报降雨
     *
     * @return
     */
    List<JSONObject> forecastRainfall();

    /**
     * 降雨统计
     *
     * @param type 1h,3h,24h
     * @return
     */
    List<JSONObject> rainfallStatistic(String type);

    /**
     * 报警统计
     *
     * @param type 1h,3h,24h,水位
     * @return
     */
    List<JSONObject> warnStatsNum(String type);

    /**
     * 水库纳蓄能力(type=1)/剩余库容(type=2)/已拦蓄水量(type=3)之和
     *
     * @param type
     * @return
     */
    List<JSONObject> rsvrStorageTotal(Integer type);

    /**
     * 水库纳蓄能力(type=1)/剩余库容(type=2)/已拦蓄水量(type=3)/最快达到汛限水位水库(type=4)列表
     *
     * @param type
     * @return
     */
    List<JSONObject> rsvrStorageList(Integer type);

    /**
     * 雨量-点位
     *
     * @return
     */
    List<JSONObject> realtimeRainSt();

    /**
     * 水位-点位
     *
     * @return
     */
    List<JSONObject> realtimeWaterSt();

    /**
     * 大中型水库-点位
     *
     * @return
     */
    List<JSONObject> rsvrPoint();

    /**
     * 水闸-点位
     *
     * @return
     */
    List<JSONObject> wagaPoint();

    /**
     * 内涝积水点-点位
     *
     * @return
     */
    List<JSONObject> floodPoint();

    /**
     * 内河水位
     *
     * @return
     */
    List<JSONObject> innerRiverLevel();

    /**
     * 台风路径
     *
     * @return
     */
    List<JSONObject> typhoonPath();

    /**
     * 水库风险
     *
     * @return
     */
    List<JSONObject> rsvrRisk(String type);

    /**
     * 水闸风险
     *
     * @return
     */
    List<JSONObject> wagaRisk(String type);

    /**
     * 排水能力
     *
     * @return
     */
    List<JSONObject> drainageAbility();

    /**
     * 总排水能力
     *
     * @return
     */
    List<JSONObject> drainageAbilityTotal();

    /**
     * 水位超警统计
     *
     * @return
     */
    List<JSONObject> riverWarnStats();

    /**
     * 积水点位统计
     *
     * @return
     */
    List<JSONObject> floodPointWarnStats();


    /**
     * 抗旱统计
     *
     * @return
     */
    List<JSONObject> antiDroughtStats();
}
