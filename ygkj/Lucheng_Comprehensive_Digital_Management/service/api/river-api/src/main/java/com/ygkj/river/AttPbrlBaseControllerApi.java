package com.ygkj.river;

import com.ygkj.river.vo.request.AttPbrlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "桥梁接口")
public interface AttPbrlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPbrlBaseReqVo vo);

}
