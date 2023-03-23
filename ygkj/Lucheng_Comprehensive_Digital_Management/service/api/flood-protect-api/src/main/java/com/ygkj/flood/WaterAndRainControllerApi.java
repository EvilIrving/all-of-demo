package com.ygkj.flood;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水雨情分析", value = "水雨情分析")
public interface WaterAndRainControllerApi {

    @ApiOperation("前N位水库排列")
    CommonResult topNReservoir(Integer index);

    @ApiOperation("大中型水库水位")
    CommonResult BigAndMidReservoirWaterStation(String basin);

    @ApiOperation("雨量站top10")
    CommonResult rainfallTop10(Integer hours);

    @ApiOperation("超汛限水库个数")
    CommonResult beyondLimitLevelNumber();

    @ApiOperation("1小时雨量超警个数")
    CommonResult superRainNumber();
}
