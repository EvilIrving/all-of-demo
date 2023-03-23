package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.project.model.IrsInvestProjectBase;
import com.ygkj.project.model.IrsInvestProjectConstruction;
import com.ygkj.project.model.IrsInvestProjectFinance;
import com.ygkj.project.model.IrsInvestProjectPlan;
import com.ygkj.project.vo.request.InvestProjectReqVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.IrsInvestProjectBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lxl
 * @create 2022-06-21 15:47
 * @description
 */
@Api(tags = "投资项目")
@RestController
@RequestMapping("/investProj")
public class InvestProjectController {

    @Resource
    private IrsInvestProjectBaseService investProjectBaseService;


    @ApiOperation("投资项目 -列表")
    @PostMapping("/list")
    private CommonResult investProjectList(InvestProjectReqVo vo){

        vo.page();
        List<IrsInvestProjectBase> list = investProjectBaseService.loadPage(vo);
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation("投资项目 -导出")
    @PostMapping("/export")
    private void exportInvestProject(InvestProjectReqVo vo, HttpServletResponse response){

        QueryWrapper<IrsInvestProjectBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("VALIDITY_FLAG","有效");
        queryWrapper.likeLeft(StringUtils.isNotBlank(vo.getYear()),"APPLY_DATE",vo.getYear());

        queryWrapper.and(StringUtils.isNotBlank(vo.getKey()),
                q -> q.like(StringUtils.isNotBlank(vo.getKey()),"PROJECT_NAME",vo.getKey())
                        .or().like(StringUtils.isNotBlank(vo.getKey()),"PRINCIPAL_NAME",vo.getKey()));

        List<IrsInvestProjectBase> list = investProjectBaseService.list(queryWrapper);
        ExportUtils.exportExcel(list,"投资项目",
                new String[]{"项目名称","单位名称","涉河水域","占用水域面积㎡","批复文号","涉河时长","联系人","联系电话","受理日期"},
                new String[]{"projectName","enterpriseName","wadingWaters","occupiedWaterArea","","wadingTime","principalName","principalTel",""},response);

    }


    @ApiOperation("投资项目 -编辑")
    @PostMapping("/edit")
    private CommonResult editInvestProject(InvestProjectReqVo vo){

        if (StringUtils.isBlank(vo.getProjectCode())){
            return CommonResult.failed("项目编码不能为空");
        }
        if (investProjectBaseService.editInvestProject(vo)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }

    }

    @ApiOperation("投资项目计划 -查看")
    @PostMapping("/queryPlan")
    private CommonResult queryInvestProjectPlan(IrsInvestProjectPlan plan){

        if (StringUtils.isBlank(plan.getProjectBaseCode())){
            return CommonResult.failed("项目编码不能为空");
        }
         return CommonResult.success(investProjectBaseService.queryInvestProjectPlan(plan));

    }

    @ApiOperation("投资项目计划 -保存编辑")
    @PostMapping("/editPlan")
    private CommonResult editInvestProjectPlan(IrsInvestProjectPlan plan){

        if (StringUtils.isBlank(plan.getProjectBaseCode())){
            return CommonResult.failed("项目编码不能为空");
        }
        try{
            investProjectBaseService.editInvestProjectPlan(plan);
            return CommonResult.success("操作成功");
        }catch (Exception e) {
            return CommonResult.failed("操作失败");
        }

    }

    @ApiOperation("投资项目资金 -查看")
    @PostMapping("/queryFinance")
    private CommonResult queryInvestProjectFinance(IrsInvestProjectFinance finance){

        if (StringUtils.isBlank(finance.getProjectBaseCode())){
            return CommonResult.failed("项目编码不能为空");
        }
        return CommonResult.success(investProjectBaseService.queryInvestProjectFinance(finance));


    }

    @ApiOperation("投资项目资金 -保存编辑")
    @PostMapping("/editFinance")
    private CommonResult editInvestProjectFinance(IrsInvestProjectFinance finance){

        if (StringUtils.isBlank(finance.getProjectBaseCode())){
            return CommonResult.failed("项目编码不能为空");
        }
        try{
            investProjectBaseService.editInvestProjectFinance(finance);
            return CommonResult.success("操作成功");
        }catch (Exception e){
            return CommonResult.failed("操作失败");
        }

    }

    @ApiOperation("投资项目建设 -查看")
    @PostMapping("/queryConstruction")
    private CommonResult queryInvestProjectConstruction(IrsInvestProjectConstruction construction){

        if (StringUtils.isBlank(construction.getProjectBaseCode())){
            return CommonResult.failed("项目编码不能为空");
        }
       return CommonResult.success(investProjectBaseService.queryInvestProjectConstruction(construction));


    }

    @ApiOperation("投资项目建设 -保存编辑")
    @PostMapping("/editConstruction")
    private CommonResult editInvestProjectConstruction(IrsInvestProjectConstruction construction){

        if (StringUtils.isBlank(construction.getProjectBaseCode())){
            return CommonResult.failed("项目编码不能为空");
        }
        try{
            investProjectBaseService.editInvestProjectConstruction(construction);
            return CommonResult.success("操作成功");
        }catch (Exception e){
            return CommonResult.failed("操作失败");
        }

    }
}
