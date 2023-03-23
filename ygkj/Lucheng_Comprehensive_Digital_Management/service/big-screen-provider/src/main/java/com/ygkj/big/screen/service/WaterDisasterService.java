package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.big.screen.model.RealTyphoonData;
import com.ygkj.big.screen.vo.response.WaResStaWaterLevelInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author fml
 * @date 2021-03-31 11:40
 * @description 水灾害防御专题
 */
public interface WaterDisasterService {

    /**
     * 雨量预警（1小时降雨量超警数量、3小时降雨量超警数量）
     *
     * @param hour 小时，1h、3h
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> rainfallWarn(Integer hour);

    /**
     * 雨量预警（1小时、3小时量）统计不同降雨量区间，雨量站的数量 ＜10、10-25、25-50、50-100、100-250、＞250
     *
     * @param hour 小时数
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> rainfallInteravalStats(Integer hour);

    /**
     * 水库纳蓄量（大中型水库累加总库容-大中型水库累加实时库容）
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> largeResCap();

    /**
     * 水库纳蓄能力（纳蓄能力前五，纳蓄能力=（汛限水库对应库容-实时水位对应库容）/集水面积）
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WaResStaWaterLevelInfo>> resCapAbility();

    /**
     * 台风等级统计(≤8级、9~10级、11~12级、13~14级、＞14级)
     *
     * @param year 年份区间
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> typhoonPowerStats(Integer year);

    /**
     * 历史台风数量、历史最大台风等级
     *
     * @param year 年份区间
     * @param type 1历史台风数量、2历史最大台风等级
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> hisTyphoonStats(Integer year, String type);

    /**
     * 统计不同年份台风数量
     *
     * @param year 年份区间
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> yearTyphoonStats(Integer year);

    /**
     * 台风信息
     *
     * @param id        id,单查
     * @param year      年份
     * @param landPower 强度
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<RealTyphoonData>> typhoonInfos(String id, String year, String landPower);


}
