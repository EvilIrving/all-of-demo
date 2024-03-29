package com.ygkj.river;

import com.ygkj.river.vo.request.AttRsmlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水库管理范围线接口")
public interface AttRsmlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRsmlBaseReqVo vo);

}
