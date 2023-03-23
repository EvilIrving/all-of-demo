package com.ygkj.river.controller;

import com.ygkj.river.ChiefPatrolControllerApi;
import com.ygkj.river.service.ChiefPatrolService;
import com.ygkj.river.vo.request.PatrolProblemQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/8/16
 */
@RestController
@RequestMapping("chiefPatrol")
public class ChiefPatrolController implements ChiefPatrolControllerApi {

    @Autowired
    ChiefPatrolService chiefPatrolService;

    @GetMapping("queryPatrolProblem")
    @Override
    public CommonResult queryPatrolProblem(PatrolProblemQueryVo reqVo) {
        return CommonResult.success(chiefPatrolService.queryPatrolProblem(reqVo));
    }

    @GetMapping("statsByQueryCondition")
    @Override
    public CommonResult statsByQueryCondition(PatrolProblemQueryVo reqVo) {
        return CommonResult.success(chiefPatrolService.statsByQueryCondition(reqVo));
    }

    @GetMapping("problemStats")
    @Override
    public CommonResult problemStats(@RequestParam("type") int type) {
        return CommonResult.success(chiefPatrolService.problemStats(type));
    }
}
