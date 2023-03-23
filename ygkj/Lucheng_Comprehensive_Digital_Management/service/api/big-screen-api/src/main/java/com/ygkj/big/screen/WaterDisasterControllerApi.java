package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author fml
 * @date 2021-04-01 9:42
 * @description 水灾害防御专题
 */
@Api(tags = "水灾害防御专题", value = "水灾害防御专题")
public interface WaterDisasterControllerApi {

    @ApiOperation("雨量预警数（1h、3h）")
    @ApiImplicitParam(name = "hour", value = "小时数")
    CommonResult rainfallWarn(Integer hour);

    @ApiOperation("时间区间内不同雨量区间雨量站数量统计")
    @ApiImplicitParam(name = "hour", value = "小时数")
    CommonResult rainfallInteravalStats(Integer hour);

    @ApiOperation("大中型水库纳蓄量")
    CommonResult largeResCap();

    @ApiOperation("水库纳蓄能力(前五)")
    CommonResult resCapAbility();

    @ApiOperation("台风等级统计")
    @ApiImplicitParam(name = "year", value = "近几年-年数")
    CommonResult typhoonPowerStats(Integer year);

    @ApiOperation("历史台风数量、历史最大台风等级")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "近几年-年数"),
            @ApiImplicitParam(name = "type", value = "1历史台风数量、2历史最大台风等级")
    })
    CommonResult hisTyphoonStats(Integer year, String type);

    @ApiOperation("不同年份台风数量统计")
    @ApiImplicitParam(name = "year", value = "近几年-年数")
    CommonResult yearTyphoonStats(Integer year);

    @ApiOperation("地图--台风信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id，单查"),
            @ApiImplicitParam(name = "year", value = "年份"),
            @ApiImplicitParam(name = "landPower", value = "台风等级")
    })
    CommonResult typhoonInfos(String id, String year, String landPower);
}
