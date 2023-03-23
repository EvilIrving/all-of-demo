package com.ygkj.project;

import com.ygkj.project.vo.request.ProjInspectionManagementInsReqVo;
import com.ygkj.project.vo.request.ProjInspectionManagementReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "检查管理", value = "检查管理")
public interface ProjInspectionManagementControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(ProjInspectionManagementInsReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(ProjInspectionManagementReqVo vo);

}
