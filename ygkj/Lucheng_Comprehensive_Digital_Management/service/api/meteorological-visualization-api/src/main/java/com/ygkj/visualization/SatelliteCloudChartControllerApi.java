package com.ygkj.visualization;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "卫星云图", value = "卫星云图")
public interface SatelliteCloudChartControllerApi {
    @ApiOperation("卫星云图")
    CommonResult satelliteCloudChart();
}
