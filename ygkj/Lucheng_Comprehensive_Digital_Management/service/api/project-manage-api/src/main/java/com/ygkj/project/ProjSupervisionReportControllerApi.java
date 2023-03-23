package com.ygkj.project;

import com.ygkj.project.vo.request.ProjSupervisionReportInsVo;
import com.ygkj.project.vo.request.ProjSupervisionReportReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "监理月报/质量评定/缺陷备案", value = "监理月报/质量评定/缺陷备案")
public interface ProjSupervisionReportControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(ProjSupervisionReportInsVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(ProjSupervisionReportReqVo vo);

}
