package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvmlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "河道管理范围线接口")
public interface AttRvmlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvmlBaseReqVo vo);

}
