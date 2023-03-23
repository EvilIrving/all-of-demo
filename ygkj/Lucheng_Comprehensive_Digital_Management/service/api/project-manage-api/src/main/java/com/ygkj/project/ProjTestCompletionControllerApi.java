package com.ygkj.project;

import com.ygkj.project.vo.request.ProjTestCompletionInsVo;
import com.ygkj.project.vo.request.ProjTestCompletionReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "检测完成情况", value = "检测完成情况")
public interface ProjTestCompletionControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(ProjTestCompletionInsVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(ProjTestCompletionReqVo vo);

}
