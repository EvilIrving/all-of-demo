package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvaaBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "河道水域面接口")
public interface AttRvaaBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvaaBaseReqVo vo);

}
