package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/8/18
 */
@Api(tags = "水政务协同大屏")
public interface WatGovCoordinateControllerApi {

    @ApiOperation("资产管理")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "1. 统计资产总数，到期件数，资产总额；2，按部门总计资产件数；3，按部门总计资产总额；4，大屏C位统计;5. 按部门统计报废，6. 按部门统计领用")})
    CommonResult stats4OaAssets(int type);
}
