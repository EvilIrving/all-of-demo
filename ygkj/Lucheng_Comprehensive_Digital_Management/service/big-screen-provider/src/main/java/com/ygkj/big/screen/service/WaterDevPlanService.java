package com.ygkj.big.screen.service;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/7/28
 */
public interface WaterDevPlanService {

    /**
     * 发展规划统计
     *
     * @param ptId
     * @return
     */
    CommonResult statsOfDevPlan(String ptId);

    /**
     * 发展规划C位统计
     *
     * @param ptId
     * @return
     */
    CommonResult statsCwindowOfDevPlan(String ptId);

    /**
     * 水研究统计
     *
     * @return
     */
    CommonResult statsOfWaterResearch(Integer type);

    /**
     * 水研究C位统计
     *
     * @return
     */
    CommonResult statsCwindowOfWaterResearch(Integer type);
}
