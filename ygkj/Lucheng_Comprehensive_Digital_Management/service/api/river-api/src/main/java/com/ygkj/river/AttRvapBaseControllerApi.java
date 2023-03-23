package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvapBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "河道临水线点接口")
public interface AttRvapBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvapBaseReqVo vo);

}
