package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvulBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "暗河接口")
public interface AttRvulBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvulBaseReqVo vo);

}
