package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 16:59
 */
@Api(tags = "风险-隐患点详情")
public interface RiskDetailsHiddendangerControllerApi {

    @ApiOperation("隐患点销号情况")
    CommonResult cancellationHiddenDangerStatic();

    @ApiOperation("根据sbId查询隐患点详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sbId", value = "钱塘江sbId"),
    })
    CommonResult findDetailsHiddenDangerBySbId(String sbId);

}
