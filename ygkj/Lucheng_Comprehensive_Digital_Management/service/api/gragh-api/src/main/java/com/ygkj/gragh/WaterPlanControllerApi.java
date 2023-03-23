package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.ProjectReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水发展规划")
public interface WaterPlanControllerApi {

    @ApiOperation("工程列表")
    CommonResult listProject(ProjectReqVo projectReqVo);

    @ApiOperation("在建工程详细信息")
    CommonResult selectBuild(String id);

    @ApiOperation("规划项目详细信息")
    CommonResult selectProgramme(String id);

    @ApiOperation("计划工程详细信息")
    CommonResult selectPlan(String id);

    @ApiOperation("谋划工程详细信息")
    CommonResult selectPlot(String id);

    @ApiOperation("前期工程详细信息")
    CommonResult selectEarly(String id);
}
