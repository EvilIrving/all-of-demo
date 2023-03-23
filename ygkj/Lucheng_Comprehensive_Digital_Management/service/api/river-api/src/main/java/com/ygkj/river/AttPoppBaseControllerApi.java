package com.ygkj.river;

import com.ygkj.river.vo.request.AttPoppBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "其他工程接口")
public interface AttPoppBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPoppBaseReqVo vo);

}
