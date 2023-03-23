package com.ygkj.project;

import com.ygkj.project.vo.request.ProjChangeManagementInsVo;
import com.ygkj.project.vo.request.ProjChangeManagementReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "变更管理/审批情况/强制条文/质量核备/质量核备/批复文件/检查计划", value = "变更管理/审批情况/强制条文/质量核备/质量核备/批复文件/检查计划")
public interface ProjChangeManagementControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(ProjChangeManagementInsVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(ProjChangeManagementReqVo vo);

}
