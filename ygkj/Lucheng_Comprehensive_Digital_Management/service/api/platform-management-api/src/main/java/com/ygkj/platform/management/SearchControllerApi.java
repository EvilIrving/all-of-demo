package com.ygkj.platform.management;

import com.ygkj.platform.management.vo.request.SearchVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "搜索服务", value = "搜索服务")
public interface SearchControllerApi {

    @ApiOperation("工程模块全文检索")
    CommonResult searchProject(SearchVo vo);
}
