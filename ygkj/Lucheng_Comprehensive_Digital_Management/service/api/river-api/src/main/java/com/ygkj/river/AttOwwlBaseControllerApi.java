package com.ygkj.river;

import com.ygkj.river.vo.request.AttOwwlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "其他水域水面线接口")
public interface AttOwwlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttOwwlBaseReqVo vo);

}
