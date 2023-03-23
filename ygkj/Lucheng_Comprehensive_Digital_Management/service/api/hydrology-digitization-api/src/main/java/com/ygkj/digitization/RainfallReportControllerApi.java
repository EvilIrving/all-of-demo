package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.text.ParseException;

@Api(tags = "雨情报表controller")
public interface RainfallReportControllerApi {

    @ApiOperation(value = "分区雨量")
    CommonResult partitionRainfall(StationRequestVo requestVo) throws ParseException;

    @ApiOperation(value = "时段极值")
    CommonResult periodExtremumRain(StationRequestVo requestVo);

    @ApiOperation(value = "日雨量")
    CommonResult dailyRainfall(StationRequestVo requestVo);

    @ApiOperation(value = "暴雨警报")
    CommonResult rainstormWarning(StationRequestVo requestVo);

    @ApiOperation(value = "一张图使用feign ，避免干扰原有")
    CommonResult rainstormWarnForOne(StationRequestVo requestVo);

    @ApiOperation(value = "为一张图提供接口")
    CommonResult rainstormWarnNumListForOne(StationRequestVo requestVo);
}
