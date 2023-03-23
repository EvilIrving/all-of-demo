package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.AverageWaterAndRainQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "统计报表", value = "统计报表")
public interface StationStatisticControllerApi {

    @ApiOperation("统计报表---按旬月统计水位均值或累计雨量")
    CommonResult stationAverageRecords(AverageWaterAndRainQueryVo queryVo);
}
