package com.ygkj.river.controller;

import com.ygkj.river.AttBlueInspectionProblemControllerApi;
import com.ygkj.river.model.AttBlueInspectionProblem;
import com.ygkj.river.service.AttBlueInspectionProblemService;
import com.ygkj.river.vo.request.AttBlueInspectionProblemReqVo;
import com.ygkj.river.vo.request.AttPatrolFeedbackReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lxl
 * @create 2021-08-06 9:27
 * @description 巡查问题内容表
 */
@RestController
@RequestMapping(value = "/blueProblem")
public class AttBlueInspectionProblemController implements AttBlueInspectionProblemControllerApi {


    private AttBlueInspectionProblemService problemService;

    public AttBlueInspectionProblemController(@Autowired AttBlueInspectionProblemService problemService) {
        this.problemService = problemService;
    }

    @Override
    @PostMapping("/addProblem")
    public CommonResult addInspectionProblem(AttBlueInspectionProblem attBlueInspectionProblem) {
        if (problemService.addInspectionProblem(attBlueInspectionProblem)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");

    }

    @Override
    public CommonResult setProblemNormal(String problemIds) {

        if (problemService.setProblemNormal(problemIds)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }


    @Override
    @GetMapping("/setProblem")
    public CommonResult setProblemStatus(String problemId, Byte status) {

        if (problemService.setProblemAbNormal(problemId,status)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }


    @Override
    @GetMapping("/getList")
    public CommonResult getProblemListByInspectionId(AttBlueInspectionProblemReqVo reqVo) {
        if (StringUtils.isBlank(reqVo.getInspectionId())){
            return CommonResult.failed("巡查id不能为空");
        }
        return CommonResult.success(problemService.getProblemListByInspectionId(reqVo));
    }


    @Override
    @PostMapping("/patrolFeedback")
    public CommonResult patrolFeedback(@RequestBody AttPatrolFeedbackReqVo reqVo) throws Exception {

        if (problemService.patrolFeedback(reqVo)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }


    @Override
    @GetMapping("/statusCount")
    public CommonResult statusCount() {

        return CommonResult.success(problemService.statusCount());
    }
}
