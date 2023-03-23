package com.ygkj.river;

import com.ygkj.river.vo.request.AttPwhpBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "码头接口")
public interface AttPwhpBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPwhpBaseReqVo vo);

}
