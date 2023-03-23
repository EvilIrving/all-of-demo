package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/17 10:15
 */
@Api(tags = "风险-高风险点/薄弱点详情")
public interface RiskDetailsHighriskControllerApi {

    @ApiOperation("根据sbId查询高风险点详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sbId", value = "钱塘江sbId"),
    })
    CommonResult findDetailsHighriskBySbId(String sbId);

    @ApiOperation("薄弱点销号情况统计")
    CommonResult cancellationWeakPointsStatic();
}
