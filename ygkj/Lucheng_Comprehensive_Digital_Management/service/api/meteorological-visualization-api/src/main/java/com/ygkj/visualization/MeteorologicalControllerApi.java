package com.ygkj.visualization;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "气象服务", value = "气象服务")
public interface MeteorologicalControllerApi {

    @ApiOperation("地图气象站落点")
    CommonResult meteorologicalRealTimeData();

    @ApiOperation("气象站弹窗信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationCode", value = "测站编码"),
            @ApiImplicitParam(name = "days", value = "历史天数")
    })
    CommonResult meteorologicalPastTimeData(String stationCode, Integer days);
}
