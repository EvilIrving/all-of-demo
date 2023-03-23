package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.DroughtReqVo;
import com.ygkj.digitization.vo.request.RainfallReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "旱情分析")
public interface DroughtAnalysisControllerApi {

    @ApiOperation("各个测站的天降雨量")
    CommonResult listRainfall(RainfallReqVo reqVo);


    @ApiOperation("获得对应日期预警数据")
    CommonResult droughtWarning(DroughtReqVo reqVo);

    @ApiOperation("获取旱情标准信息")
    CommonResult getDroughtStandard();

    @ApiOperation("近30日日降雨量")
    CommonResult listDayRainfall();

}
