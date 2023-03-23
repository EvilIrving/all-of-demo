package com.ygkj.platform.management;

import com.ygkj.platform.management.vo.request.ExceptionPageQueryVo;
import com.ygkj.platform.management.vo.request.SysUserBehaviorAnalysisResultPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "行为数据分析", value = "行为数据分析")
public interface BehavioralDataAnalysisControllerApi {

    @ApiOperation("异常分析列表")
    CommonResult exceptions(ExceptionPageQueryVo queryVo);

    @ApiOperation("系统模块")
    CommonResult exceptionSystems();

    @ApiOperation("出错类型")
    CommonResult responseStatus();

    @ApiOperation("用户行为关联分析---按人员列表")
    CommonResult behaviorAnalysisResult(SysUserBehaviorAnalysisResultPageQueryVo queryVo);
}
