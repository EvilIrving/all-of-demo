package com.ygkj.river;

import com.ygkj.river.vo.request.AttBeautLakesReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "美丽河湖接口")
public interface AttBeautLakesControllerApi {


    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttBeautLakesReqVo vo);

    @ApiOperation(value = "美丽河湖工程数量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 1:工程总数 2:项目在建 3:已完工")
    })
    CommonResult projectStatic(String type);

    @ApiOperation(value = "各个工程类型数量统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 美丽河湖、中小河流、水美乡镇、农村池塘")
    })
    CommonResult eachProjectStatistics(String type);
}
