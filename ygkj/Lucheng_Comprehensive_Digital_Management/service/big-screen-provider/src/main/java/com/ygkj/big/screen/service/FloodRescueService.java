package com.ygkj.big.screen.service;

import com.ygkj.big.screen.vo.response.FrWaterLevelStaRespVo;
import com.ygkj.big.screen.vo.response.WzAdcdInfo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author fml
 * @date 2021-03-29 14:19
 * @description 防汛抢险专题
 */
public interface FloodRescueService {

    /**
     * 十大工程点位（水库、山塘、提防、海塘、水电站、泵站、水闸、闸站、灌区、农饮水）积水点、内河水位点、仓库、队伍以及摄像机
     *
     * @param type 1水库，2山塘，3提防，4海棠，5水电站...
     * @param id   id单查
     * @return CommonResult
     */
    CommonResult mapPoints(String type, String id);

    /**
     * 仓库数量统计
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WzAdcdInfo>> wareHouseStats();

    /**
     * 抢险队伍统计
     *
     * @return CommonResult
     */
    CommonResult<List<WzAdcdInfo>> teamStats();

    /**
     * 人员数量统计（队伍人员数量统计，按行政区划）
     *
     * @return CommonResult
     */
    CommonResult<List<WzAdcdInfo>> teamPersonStats();

    /**
     * 水利工程统计（按区划、按工程类型）
     *
     * @param type 1按区划、2按工程类型
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WzAdcdInfo>> projStats(String type);

    /**
     * 水位轮播（水库+河道）
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<FrWaterLevelStaRespVo>> waterLevels();

    /**
     * 工情轮播
     *
     * @param type 1水闸，2水泵，3水库
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult projConditions(String type);
}
