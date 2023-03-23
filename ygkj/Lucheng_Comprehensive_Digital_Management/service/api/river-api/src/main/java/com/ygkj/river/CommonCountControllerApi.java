package com.ygkj.river;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "一张图统计", value = "一张图统计")
public interface CommonCountControllerApi {

    @ApiOperation("统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "统计类型：河流/湖泊/河景...", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "column", value = "统计字段：行政区划/河流等级/流域...", dataType = "string", paramType = "query", required = true)
    })
    CommonResult count(String type, String column);
}
