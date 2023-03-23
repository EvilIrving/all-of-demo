package com.ygkj.visualization;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "气象场数据", value = "气象场数据")
public interface SiteControllerApi {

    @ApiOperation("风场时间轴")
    CommonResult windFarmList();

    @ApiOperation("风场详细数据")
    CommonResult windFarm(Integer id);

    @ApiOperation("浪场时间轴")
    CommonResult waveFieldList();

    @ApiOperation("浪场详细数据")
    CommonResult waveField(Integer id);
}
