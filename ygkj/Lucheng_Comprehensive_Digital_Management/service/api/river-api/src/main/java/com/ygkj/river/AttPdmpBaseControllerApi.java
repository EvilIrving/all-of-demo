package com.ygkj.river;

import com.ygkj.river.vo.request.AttPdmpBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "拦水坝（堰）接口")
public interface AttPdmpBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPdmpBaseReqVo vo);

}
