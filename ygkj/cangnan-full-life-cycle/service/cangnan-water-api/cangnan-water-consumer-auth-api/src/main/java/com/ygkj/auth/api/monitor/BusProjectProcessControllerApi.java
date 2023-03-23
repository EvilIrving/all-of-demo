package com.ygkj.auth.api.monitor;

import com.ygkj.auth.VO.request.ProjectAssigneeOpinionReqVo;
import com.ygkj.auth.VO.request.ProjectCheckInfoReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: huangzh
 * @Date: 2020/9/21 10:09
 * @Description:
 */
@Api(value = "BusProjectProcessControllerApi", tags = "审批-处理流程")
public interface BusProjectProcessControllerApi {

    @ApiOperation(value = "根据项目id获取办理流程")
    Object getProcessRecords(String id);

    @ApiOperation(value = "不予受理")
    Object notAccept(ProjectAssigneeOpinionReqVo vo);

    @ApiOperation(value = "补正不齐")
    Object correction(ProjectAssigneeOpinionReqVo vo);

    @ApiOperation(value = "受理")
    Object accept(ProjectCheckInfoReqVo vo);

    @ApiOperation(value = "办结")
    Object finish(ProjectAssigneeOpinionReqVo vo);

    @ApiOperation(value = "交办上一人")
    Object callback(ProjectAssigneeOpinionReqVo vo);

    @ApiOperation(value = "审核")
    Object check(ProjectCheckInfoReqVo vo);

    @ApiOperation(value = "签批")
    Object approval(ProjectCheckInfoReqVo vo);

    @ApiOperation(value = "保存")
    Object saveCheckInfo(ProjectCheckInfoReqVo vo);

    @ApiOperation(value = "校对")
    Object proofreader(ProjectAssigneeOpinionReqVo vo);

    @ApiOperation(value = "根据项目id获取批复或行政许可信息")
    Object getProjectLicense(String id);

    @ApiOperation(value = "根据项目id获取审核信息")
    Object getProjectCheckInfo(String id);
}
