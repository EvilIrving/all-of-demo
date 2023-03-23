package com.ygkj.river;

import com.ygkj.river.vo.request.AttRsapBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水库临水线点接口")
public interface AttRsapBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRsapBaseReqVo vo);

}
