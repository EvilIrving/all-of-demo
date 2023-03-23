package com.ygkj.river;

import com.ygkj.river.vo.request.AttPdklBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "堤防接口")
public interface AttPdklBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttPdklBaseReqVo vo);

}
