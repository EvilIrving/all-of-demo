package com.ygkj.project;

import com.ygkj.project.vo.request.AllProjectReqVo;
import com.ygkj.project.vo.request.DelProjectReqVo;
import com.ygkj.project.vo.request.ProjectEditReqVo;
import com.ygkj.project.vo.response.AllProjectResVo;
import com.ygkj.project.vo.response.DictResVo;
import com.ygkj.project.vo.response.ThreeChargeResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "水利工程-工程运维管理-工程管理")
public interface EngineeringSupervisionControllerApi {

    @ApiOperation(value = "工程类型统计", response = DictResVo.class)
    @ApiImplicitParam(name = "type", value = "统计方式 1流域 2类型 3行政区划", required = true, paramType = "query")
    CommonResult countProject(Integer type);

    @ApiOperation("流域列表")
    CommonResult listBas();

    @ApiOperation("工程规模列表")
    CommonResult listScale();

    @ApiOperation("工程等别列表")
    CommonResult listGrad();

    @ApiOperation(value = "工程列表", response = AllProjectResVo.class)
    CommonResult listProject(AllProjectReqVo allProjectReqVo);

    @ApiOperation(value = "工程列表导出")
    void exportProjectList(AllProjectReqVo allProjectReqVo, HttpServletResponse response);

    @ApiOperation(value = "工程详情", response = AllProjectResVo.class)
    @ApiImplicitParam(name = "projectCode", value = "工程编码", required = true, paramType = "query")
    CommonResult projectDetail(String projectCode);

    @ApiOperation(value = "三个责任人详细信息", response = ThreeChargeResVo.class)
    CommonResult threeCharge(String projectCode);

    @ApiOperation("编辑工程信息")
    CommonResult editProject(ProjectEditReqVo projectEditReqVo);

    @ApiOperation("批量删除工程")
    CommonResult delProject(DelProjectReqVo reqVo);


    @ApiOperation(value = "向对应责任人发送消息提醒")
    CommonResult sendMsg(String phone, String content, String sendType);

}
