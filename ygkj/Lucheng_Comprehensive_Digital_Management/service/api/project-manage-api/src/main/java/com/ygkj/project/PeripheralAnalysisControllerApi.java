package com.ygkj.project;

import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "周边分析", value = "周边分析")
public interface PeripheralAnalysisControllerApi {

    @ApiOperation("周边分析")
    CommonResult analysis(ProjectRangeQueryVo queryVo);

    @ApiOperation("根据节点查询对应的记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codes", value = "编码，对应树节点中的codes"),
            @ApiImplicitParam(name = "type", value = "对应节点中的name")
    })
    CommonResult records(String codes, String type);
}
