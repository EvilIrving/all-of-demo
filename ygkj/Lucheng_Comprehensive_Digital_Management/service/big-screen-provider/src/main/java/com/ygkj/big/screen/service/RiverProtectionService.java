package com.ygkj.big.screen.service;

import com.ygkj.big.screen.vo.request.RpProblemStatsReqVo;
import com.ygkj.big.screen.vo.response.*;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author fml
 * @date 2021-03-22 17:16
 * @description 河湖保护专题
 */
public interface RiverProtectionService {

    /**
     * 温州行政区划信息
     *
     * @return List<WzAdcdInfo>
     */
    List<WzAdcdInfo> getWzAdcds();

    /**
     * 河流数量统计
     *
     * @param adcd:
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<RpRiverNumStatitstcsRespVo>> riverNumStatistics(String adcd);

    /**
     * 水位列表
     *
     * @param adcd:
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<RpWaterLevelsRespVo>> waterLevels(String adcd);

    /**
     * 四乱问题统计
     *
     * @param vo 请求条件
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<RpProblemStatsRespVo>> problemStatistics(String type);

    /**
     * 雨量分析
     *
     * @param adcd 行政区划
     * @param type 类型（1行政区划，2流域）
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<RpRainfallRespVo>> railfallStatistics(String adcd, String type);

    /**
     * 地图点位
     *
     * @param type 1水源地、2水库、 3水质监测、 4水位监测、5雨量监测、 6河湖全景图、 7摄像机、 8四乱点位
     * @param id   单查
     * @return CommonResult
     */
    CommonResult<RpMapPointsRespVo> mapPoint(String type, String id);

    /**
     * 水位统计
     *
     * @return CommonResult
     */
    CommonResult<List<RpWarnNumStatsRespVo>> waterLevelStatistics();

    /**
     * 水质统计
     *
     * @return CommonResult
     */
    CommonResult<List<RpWarnNumStatsRespVo>> szStatistics();

    /**
     * 雨量统计
     *
     * @return CommonResult
     */
    CommonResult<List<RpWarnNumStatsRespVo>> rainfallStatistics();

    /**
     * 视频统计
     *
     * @return CommonResult
     */
    CommonResult<List<RpWarnNumStatsRespVo>> cameraStatistics();

    /**
     * 行政区划内设备统计
     *
     * @param type 1水位/2水质/3雨量/4视频
     * @return CommonResult
     */
    CommonResult<List<WzAdcdInfo>> adcdEquipmentStats(String type);

    /**
     * 行政区划河湖库数量统计
     *
     * @param type 1河流/2湖泊/3水位
     * @return CommonResult
     */
    CommonResult<List<WzAdcdInfo>> adcdRLRStats(String type);
}
