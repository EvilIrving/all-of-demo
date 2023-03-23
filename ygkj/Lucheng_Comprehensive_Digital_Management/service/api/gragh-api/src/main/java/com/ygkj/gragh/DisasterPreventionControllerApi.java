package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.TyphoonReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水灾害防御")
public interface DisasterPreventionControllerApi {

    @ApiOperation("实时台风列表")
    CommonResult realTimeTyphoon();

    @ApiOperation("台风详细信息")
    CommonResult selectTyphoon(Integer id);

    @ApiOperation("历史台风列表")
    CommonResult historyTyphoon(TyphoonReqVo typhoonReqVo);
}
