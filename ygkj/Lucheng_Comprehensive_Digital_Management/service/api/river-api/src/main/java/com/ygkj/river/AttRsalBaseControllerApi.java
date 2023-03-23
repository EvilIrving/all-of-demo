package com.ygkj.river;

import com.ygkj.river.vo.request.AttRsalBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水库临水线接口")
public interface AttRsalBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRsalBaseReqVo vo);

}
