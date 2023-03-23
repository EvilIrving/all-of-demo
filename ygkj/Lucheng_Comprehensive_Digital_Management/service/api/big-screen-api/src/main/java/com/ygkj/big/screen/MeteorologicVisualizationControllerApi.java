package com.ygkj.big.screen;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/20
 */
@Api(tags = "代数云-水旱灾害防御专题")
public interface MeteorologicVisualizationControllerApi {
    @ApiOperation("天气预报")
    List<JSONObject> forecast();

    @ApiOperation("气象统计-温度")
    List<JSONObject> meteorologicStatisticTemp();

    @ApiOperation("气象统计-降水量")
    List<JSONObject> meteorologicStatisticRain();

    @ApiOperation("气象统计-风速")
    List<JSONObject> meteorologicStatisticWind();

    @ApiOperation("台风统计")
    List<JSONObject> typhoonStatistic();

    @ApiOperation("最近台风")
    List<JSONObject> typhoonRecently();

    @ApiOperation("相似台风：type=1(路径相似)/2(季节相似)/3(生成地相似)")
    List<JSONObject> similarTyphoon(Integer type);

    @ApiOperation("降雨统计-实时降雨")
    List<JSONObject> realtimeRainfall();

    @ApiOperation("降雨统计-预报降雨")
    List<JSONObject> forecastRainfall();

    @ApiOperation("降雨统计：type=1(1h)/type=2(3h)/type=3(24h)")
    List<JSONObject> rainfallStatistic(Integer type);

    @ApiOperation("报警统计：type=1(1h)/type=2(3h)/type=3(24h)/type=4(水位)")
    List<JSONObject> warnStatsNum(Integer type);

    @ApiOperation("水库纳蓄能力(type=1)/剩余库容(type=2)/已拦蓄水量(type=3)之和")
    List<JSONObject> rsvrStorageTotal(Integer type);

    @ApiOperation("水库纳蓄能力(type=1)/剩余库容(type=2)/已拦蓄水量(type=3)/最快达到汛限水位水库(type=4)列表")
    List<JSONObject> rsvrStorageList(Integer type);

    @ApiOperation("雨量-点位")
    List<JSONObject> realtimeRainSt();

    @ApiOperation("水位-点位")
    List<JSONObject> realtimeWaterSt();

    @ApiOperation("水闸-点位")
    List<JSONObject> wagaPoint();

    @ApiOperation("大中型水库-点位")
    List<JSONObject> rsvrPoint();

    @ApiOperation("台风路径")
    List<JSONObject> typhoonPath();

    @ApiOperation("积水-点位")
    List<JSONObject> floodPoint();

    @ApiOperation("内河水位")
    List<JSONObject> innerRiverLevel();

    @ApiOperation("水库风险：type=1(超防洪高)/type=2(超汛限)")
    List<JSONObject> rsvrRisk(Integer type);

    @ApiOperation("水闸风险：type=1(超警戒)/type=2(超保证)")
    List<JSONObject> wagaRisk(Integer type);

    @ApiOperation("排水能力")
    List<JSONObject> drainageAbility();

    @ApiOperation("排水能力")
    List<JSONObject> drainageAbilityTotal();

    @ApiOperation("水位超警统计")
    List<JSONObject> riverWarnStats();

    @ApiOperation("积水点位统计")
    List<JSONObject> floodPointWarnStats();

    @ApiOperation("防洪统计")
    List<JSONObject> antiDroughtStats();

}
