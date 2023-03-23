package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/7/28
 */
@Api(tags = "水发展规划大屏")
public interface WaterDevPlanControllerApi {

    @ApiOperation("发展规划统计")
    CommonResult statsOfDevPlan(String ptId);

    @ApiOperation("发展规划C位统计")
    CommonResult statsCwindowOfDevPlan(String ptId);

    @ApiOperation("水研究统计")
    CommonResult statsOfWaterResearch(Integer year);

    @ApiOperation("水研究C位统计")
    CommonResult statsCwindowOfWaterResearch(Integer year);

}
