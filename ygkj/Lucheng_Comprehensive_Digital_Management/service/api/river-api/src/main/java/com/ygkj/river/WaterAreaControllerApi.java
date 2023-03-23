package com.ygkj.river;

import com.ygkj.river.vo.request.AttWaterAreaReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水域面积接口")
public interface WaterAreaControllerApi {

    @ApiOperation(value = "水域面积总体统计")
    CommonResult overallStatisticsWaterArea(Integer year);

    @ApiOperation(value = "水域面积柱状图")
    CommonResult histogramWaterArea();

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttWaterAreaReqVo vo);

    @ApiOperation(value = "水域变化C位统计")
    CommonResult cPositionStatisticsWaterArea(Integer year);

    @ApiOperation(value = "水域变化各个行政区划水域面积")
    CommonResult waterAreaEachAdministrative(Integer year);
}
