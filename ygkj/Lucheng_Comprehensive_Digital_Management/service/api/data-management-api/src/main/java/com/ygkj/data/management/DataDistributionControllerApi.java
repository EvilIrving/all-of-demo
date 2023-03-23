package com.ygkj.data.management;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "大屏组件---数据分布", value = "大屏组件---数据分布")
public interface DataDistributionControllerApi {

    @ApiOperation("饼图")
    CommonResult totalStatistic();

    @ApiOperation("水利基础数据")
    CommonResult basicData(String name);

    @ApiOperation("实时数据")
    CommonResult realTimeData();
}
