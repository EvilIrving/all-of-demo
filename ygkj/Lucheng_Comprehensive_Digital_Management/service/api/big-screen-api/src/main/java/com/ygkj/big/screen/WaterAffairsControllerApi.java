package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author fml
 * @date 2021-03-31 11:29
 * @description 水事务监管专题
 */
@Api(tags = "水事务监管专题", value = "水事务监管专题")
public interface WaterAffairsControllerApi {

    @ApiOperation("水土流失图斑 (遥感影像)")
    CommonResult soilEroMapPoints();

    @ApiOperation("治理工程图斑 (工程边界)")
    @ApiImplicitParam(name = "id", value = "id，单查用")
    CommonResult projMapPoints(String id);

    @ApiOperation("摄像头统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "warnStatus", value = "1正常，2异常"),
            @ApiImplicitParam(name = "stats", value = "1区划，2工程类型")
    })
    CommonResult cameraStats(String warnStatus, String stats);

    @ApiOperation("地图图例--摄像机正异常点位")
    @ApiImplicitParam(name = "status", value = "1正常，2异常")
    CommonResult cameraPoints(String status);

    @ApiOperation("水库超汛限占比")
    CommonResult resOverLimitStats();

    @ApiOperation("各区县水库平均蓄水率")
    CommonResult waterStorageStats();

    @ApiOperation("水土流失面积统计")
    @ApiImplicitParam(name = "type", value = "1按年份,2按行政区划")
    CommonResult soilErosionStats(String type);

    @ApiOperation("水土流失-治理图斑统计-治理面积")
    @ApiImplicitParam(name = "type", value = "1按年份,2按行政区划")
    CommonResult soilErosionGovernStats(String type);

    @ApiOperation("水土流失-治理项目统计")
    @ApiImplicitParam(name = "type", value = "1按年份,2按行政区划")
    CommonResult soilErosionProjStats(String type);

    @ApiOperation("水厂供水统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "1. 根据行政区划统计：城市、乡镇、联村、单村,全部;" +
            "2. 根据水厂规模统计：两百吨千人以下、两百吨千人以上、千吨万人、城市水厂；" +
            "3. 根据水厂类型统计：城市、乡镇、联村、单村；" +
            "4. 按行政区划统计: 正常供水，供水预警，水质预警，水质供水预警" +
            "5. 按水厂的运行状态统计:正常供水，供水预警，水质预警，水质供水预警" +
            "6. 统计日累计供水量，异常供水次数")
    })
    CommonResult waterSupplyStats(int type);


//    /**
//     * 水厂运行状态统计
//     * @param result  1按数量 。2按人口
//     * @param data 1统计 2柱状图
//     * @return
//     */
//    @ApiOperation("水厂运行状态统计")
//    CommonResult waterOperateStats(String result,String data);

}
