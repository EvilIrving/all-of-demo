package com.ygkj.river;

import com.ygkj.river.vo.request.AttRvwlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "河道水面线接口")
public interface AttRvwlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttRvwlBaseReqVo vo);

}
