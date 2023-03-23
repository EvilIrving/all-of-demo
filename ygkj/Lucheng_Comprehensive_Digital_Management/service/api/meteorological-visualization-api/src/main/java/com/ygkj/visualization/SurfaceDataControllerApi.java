package com.ygkj.visualization;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "面雨量/等值面相关服务", value = "面雨量/等值面相关服务")
public interface SurfaceDataControllerApi {
    @ApiOperation("五公里矫正等值面数据")
    CommonResult fiveKilometerCorrectRainData();
}
