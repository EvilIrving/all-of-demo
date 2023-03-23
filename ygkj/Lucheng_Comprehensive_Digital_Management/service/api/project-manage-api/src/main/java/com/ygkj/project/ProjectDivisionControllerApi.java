package com.ygkj.project;

import com.ygkj.project.vo.request.ProjectDivisionInsVo;
import com.ygkj.project.vo.request.ProjectDivisionReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "项目划分", value = "项目划分")
public interface ProjectDivisionControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(ProjectDivisionInsVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(ProjectDivisionReqVo vo);

    @ApiOperation(value = "list查找")
    CommonResult loadList(ProjectDivisionReqVo vo);
}
