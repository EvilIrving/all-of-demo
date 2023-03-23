package com.ygkj.river;

import com.ygkj.river.vo.request.AttPpspBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "泵站接口")
public interface AttPpspBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPpspBaseReqVo vo);

}
