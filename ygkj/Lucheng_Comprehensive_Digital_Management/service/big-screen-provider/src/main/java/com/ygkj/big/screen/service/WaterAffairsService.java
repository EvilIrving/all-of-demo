package com.ygkj.big.screen.service;

import com.ygkj.big.screen.model.HikvisionCameraRecord;
import com.ygkj.big.screen.model.WtSoilErosionProjBase;
import com.ygkj.big.screen.vo.response.WaSoilErosionRespVo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author fml
 * @date 2021-03-30 11:28
 * @description 水事务监管
 */
public interface WaterAffairsService {

    /**
     * 水土流失图斑
     *
     * @return CommonResult
     */
    CommonResult<List<WaSoilErosionRespVo>> soilEroMapPoints();

    /**
     * 治理工程图斑（工程边界）
     *
     * @param id id
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WtSoilErosionProjBase>> projMapPoints(String id);

    /**
     * 摄像头统计
     *
     * @param warnStatus 1正常，2异常
     * @param stats      1区划，2工程类型
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WzAdcdInfo>> cameraStats(String warnStatus, String stats);

    /**
     * 地图图例--摄像机正异常点位
     *
     * @param status 1正常，2异常
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<HikvisionCameraRecord>> cameraPoints(String status);

    /**
     * 水库超汛限占比
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> resOverLimitStats();

    /**
     * 各区县水库平均蓄水率=实时库容/总库容
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WzAdcdInfo>> waterStorageStats();

    /**
     * 水土流失面积统计
     *
     * @param type 1按年份,2按行政区划
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult soilErosionStats(String type);

    /**
     * 水土流失-治理图斑统计-治理面积
     *
     * @param type （1按年份,2按行政区划）
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult soilErosionGovernStats(String type);

    /**
     * 水土流失-治理项目统计
     *
     * @param type （1按年份,2按行政区划）
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult soilErosionProjStats(String type);

    /**
     * 水厂统计
     *
     * @param type 1. 根据行政区划统计：城市、乡镇、联村、单村;
     *             2. 根据水厂规模统计：两百吨千人以下、两百吨千人以上、千吨万人、城市水厂；
     *             3. 根据水厂类型统计：城市、乡镇、联村、单村；
     *             4. 按行政区划统计: 正常供水，供水预警，水质预警，水质供水预警
     *             5. 按水厂的运行状态统计:正常供水，供水预警，水质预警，水质供水预警
     *             6. 统计日累计供水量，异常供水次数
     */
    CommonResult waterSupplyStats(int type);

//    /**
//     * 水厂运行状态统计
//     * @param result  1按数量 。2按人口
//     * @param data 1统计 2柱状图
//     * @return
//     */
//    Object waterOperateStats(String result, String data);
}
