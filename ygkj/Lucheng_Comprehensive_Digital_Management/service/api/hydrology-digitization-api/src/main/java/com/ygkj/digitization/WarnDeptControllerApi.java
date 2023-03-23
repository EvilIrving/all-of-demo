package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.WarnDeptReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "预警部门", value = "预警部门")
public interface WarnDeptControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(WarnDeptReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(WarnDeptReqVo vo);

    @ApiOperation(value = "部门树形结构")
    CommonResult findTree(WarnDeptReqVo vo);

}
