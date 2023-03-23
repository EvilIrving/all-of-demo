package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvolBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "其他沟渠接口")
public interface AttRvolBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvolBaseReqVo vo);

}
