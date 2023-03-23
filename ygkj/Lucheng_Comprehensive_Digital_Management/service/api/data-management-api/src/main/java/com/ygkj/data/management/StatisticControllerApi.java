package com.ygkj.data.management;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水利数据大屏", value = "水利数据大屏")
public interface StatisticControllerApi {

    @ApiOperation("水利基础数据")
    CommonResult basicData();

    @ApiOperation("水利实时数据")
    CommonResult synchronizedData();

    @ApiOperation("水利业务数据")
    CommonResult businessData();

    @ApiOperation("服务分类")
    CommonResult apiCategory();

    @ApiOperation("近七日入仓数据量")
    CommonResult selectPastSevenDayPersistentDataNumber();

    @ApiOperation("最新上线的api")
    CommonResult newestApi();

    @ApiOperation("服务调用次数")
    CommonResult apiMaxInvokeNumber();

    @ApiOperation("大屏上方统计")
    CommonResult totalStatistic();

    @ApiOperation("数据库连接列表")
    CommonResult connections();

    @ApiOperation("服务申请排名")
    CommonResult apiApplyRank();

    @ApiOperation("服务请求大于1秒的服务")
    CommonResult countApiDurationOverOneSecond();

    @ApiOperation("报错接口轮播列表")
    CommonResult wrongApis();

    @ApiOperation("数据质量统计")
    CommonResult compliance();

    @ApiOperation("各单位服务申请次数")
    CommonResult apiApplyNumbers();
}
