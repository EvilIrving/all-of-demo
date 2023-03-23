package com.ygkj.river;

import com.ygkj.river.vo.request.AttRsaaBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水库水域面接口")
public interface AttRsaaBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRsaaBaseReqVo vo);

}
