package com.ygkj.river;

import com.ygkj.river.model.AttBlueInspectionProblem;
import com.ygkj.river.vo.request.AttBlueInspectionProblemReqVo;
import com.ygkj.river.vo.request.AttPatrolFeedbackReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2021-08-05 21:19
 * @description
 */
@Api(tags = "蓝线管理巡查异常问题接口")
public interface AttBlueInspectionProblemControllerApi {

    /**
     * 新增巡查异常问题   复核时候临时添加对应异常内容
     *
     * @param attBlueInspectionProblem
     * @return
     */
    @ApiOperation("新增巡查异常问题")
    CommonResult addInspectionProblem(AttBlueInspectionProblem attBlueInspectionProblem);


    /**
     * 设置该巡查下问题已整改
     *
     * @param problemIds
     * @return
     */
    @ApiOperation("设置该巡查下问题已整改，派发人鉴定无隐患，传参为 对应的problemIds ，隔开")
    CommonResult setProblemNormal(String problemIds);

    /**
     * 设置改巡查问题异常，或者正常  设置异常的话同步更改为未整改
     *
     * @param problemId 问题id
     * @param status    状态 （1正常、2异常）
     * @return
     */
    @ApiOperation("设置改巡查问题异常，或者正常")
    CommonResult setProblemStatus(String problemId, Byte status);

    /**
     * 根据巡查问题获取问题列表
     *
     * @param reqVo
     * @return
     */
    @ApiOperation("获取问题列表根据巡查id （获取首次巡检问题，复核问题，整改问题列表）")
    CommonResult getProblemListByInspectionId(AttBlueInspectionProblemReqVo reqVo);

    /**
     * 巡查反馈
     * @param type
     * @param problems
     * @return
     */
    @ApiOperation("移动端接口 - 巡查反馈 type 1-首次巡查  2-复核巡查  problems-问题列表")
    CommonResult patrolFeedback(AttPatrolFeedbackReqVo reqVo) throws Exception;


    @ApiOperation("移动端接口 -页签数量统计 巡查问题 全部/各个阶段数量 统计 ")
    CommonResult statusCount();
}
