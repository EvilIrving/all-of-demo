package com.ygkj.river;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水域占用补偿接口")
public interface AttWaterareaCompensationControllerApi {

    @ApiOperation(value = "水域占补面积总体统计")
    CommonResult statisticsReclaimedAreaWater(Integer year);

    @ApiOperation(value = "水域占补面积按年份统计")
    CommonResult statisticsReclaimedByYear();

    @ApiOperation(value = "水域占补各个行政区划水域面积")
    CommonResult waterCompensationEachAdministrative(Integer year);
}
