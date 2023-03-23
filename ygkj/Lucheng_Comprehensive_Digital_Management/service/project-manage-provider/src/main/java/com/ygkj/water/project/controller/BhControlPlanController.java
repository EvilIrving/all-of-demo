package com.ygkj.water.project.controller;

import com.ygkj.project.model.BhControlPlan;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.BhControlPlanService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bhControlPlan")
public class BhControlPlanController {
    @Autowired
    private BhControlPlanService bhControlPlanService;

    @ApiOperation("控运计划新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(BhControlPlan plan){
        try{
            if (StringUtils.isEmpty(plan.getId())) {
                bhControlPlanService.insert(plan);
                return CommonResult.success("保存成功!");
            } else {
                bhControlPlanService.update(plan);
                return CommonResult.success("修改成功!");
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }

    @ApiOperation("控运计划查询")
    @PostMapping("/loadPage")
    public CommonResult loadPage(BhControlPlan plan){
        try{
            return CommonResult.success(bhControlPlanService.loadPage(plan));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @ApiOperation("控运计划删除")
    @PostMapping("/del")
    public CommonResult del(BhControlPlan plan){
        plan.setDelFlag(1);
        if (bhControlPlanService.delete(plan)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
