package com.ygkj.project;

import com.ygkj.project.vo.request.ProjMicroImageInsVo;
import com.ygkj.project.vo.request.ProjMicroImageReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "微影像管理", value = "微影像管理")
public interface ProjMicroImageControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(ProjMicroImageInsVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "list时间轴查找")
    CommonResult loadPage(ProjMicroImageReqVo vo);

    @ApiOperation(value = "时间轴")
    @ApiImplicitParam(name = "type", value = "类型 0:重要隐蔽工程 1:每日主要作业面", paramType = "query")
    CommonResult yearList(String type);

    @ApiOperation(value = "list分页查找")
    CommonResult listData(ProjMicroImageReqVo vo);

    @ApiOperation(value = "临时保存用户选择的工程数据")
    CommonResult saveUserChoosepData(String data);

    @ApiOperation(value = "获取临时保存用户选择的工程数据")
    CommonResult getUserChoosepData();
}
