package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author fml
 * @date 2021-04-02 17:56
 * @description 水资源保障专题
 */
@Api(tags = "水资源保障专题", value = "水资源保障专题")
public interface WaterProtectionControllerApi {

    @ApiOperation("水源地统计-行政区划")
    CommonResult waterSourceAdcdStats();

    @ApiOperation("水源地统计-行政区划")
    CommonResult waterSourceTypeStats();

    @ApiOperation("水源地列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "水源地类型（0.地下水1.水库2.山塘3.河流）"),
            @ApiImplicitParam(name = "scale", value = "规模取水量(暂时无用)")
    })
    CommonResult waterSources(String type, String scale);

    @ApiOperation("取水许可有效期内有效取水户数量")
    CommonResult wiuValidStats();

    @ApiOperation("取水户用水规模")
    CommonResult wiuScaleStats();

    @ApiOperation("取水曲线-按月份")
    @ApiImplicitParam(name = "year", value = "年份")
    CommonResult wiuUseWaterMonthStats(String year);

    @ApiOperation("取水曲线-按行政区划")
    @ApiImplicitParam(name = "year", value = "年份")
    CommonResult wiuUseWaterAdcdStats(String year);

    @ApiOperation("数字珊溪C位统计")
    CommonResult patrolInfoProcessStatisticss();

    @ApiOperation("巡查数量统计")
    CommonResult patrolInfoPatrolChaosHappStatic();

    @ApiOperation("巡查按行政区划统计")
    CommonResult patrolChaosHappStaticByAreaCode();

    @ApiOperation("巡查乱点分析统计")
    CommonResult patrolChaosHappStaticByChaosType();

    @ApiOperation("巡查处置状态统计")
    CommonResult patrolChaosHappStaticByChaosStatus();

    @ApiOperation("巡查违法列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageSize", value = "页数"),
            @ApiImplicitParam(name = "pageSize", value = "第几页"),
            @ApiImplicitParam(name = "chaosType", value = "违法类型 占用水域、违章建筑、倾倒垃圾、非法采砂、水土流失、非法采砂、水土流失、倾倒垃圾、违章建筑、占用水域"),
            @ApiImplicitParam(name = "chaosName", value = "乱点名称"),
            @ApiImplicitParam(name = "chaosStatus", value = "乱点状态 已完成、整改中"),
    })
    CommonResult happeningListJson(Integer pageSize, Integer pageNum, String chaosType, String chaosName, String chaosStatus);

}
