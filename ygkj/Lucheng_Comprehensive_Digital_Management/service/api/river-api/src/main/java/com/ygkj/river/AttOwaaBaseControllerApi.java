package com.ygkj.river;

import com.ygkj.river.vo.request.AttOwaaBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "其他水域水域面接口")
public interface AttOwaaBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttOwaaBaseReqVo vo);

}
