package com.ygkj.river;

import com.ygkj.river.vo.request.AttAcclBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "人工水道中心线接口")
public interface AttAcclBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttAcclBaseReqVo vo);

}
