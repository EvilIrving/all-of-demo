package com.ygkj.river;

import com.ygkj.river.vo.request.AttHpalBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "塘临水线接口")
public interface AttHpalBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttHpalBaseReqVo vo);

}
