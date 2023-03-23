package com.ygkj.project;

import com.ygkj.project.vo.request.AttServiceItemReqVo;
import com.ygkj.project.vo.request.ProjectDivisionInsVo;
import com.ygkj.project.vo.request.ProjectDivisionReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2022-04-12 10:38
 * @description 工程物业管理 - 服务事项
 */
@Api(tags = "服务事项", value = "服务事项")
public interface AttServiceItemControllerApi {

    @ApiOperation(value = "查询运管工程名录")
    CommonResult loadSkPage(AttServiceItemReqVo vo);

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(AttServiceItemReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(AttServiceItemReqVo vo);


}
