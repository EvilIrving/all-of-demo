package com.ygkj.river;

import com.ygkj.river.vo.request.AttOwmlBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "其他水域管理范围线接口")
public interface AttOwmlBaseControllerApi {

    @ApiOperation("分页查找")
    CommonResult loadPage(AttOwmlBaseReqVo vo);

}
