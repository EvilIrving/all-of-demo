package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.project.model.WarningRule;
import com.ygkj.project.model.WarningUser;
import com.ygkj.project.vo.request.RuleStReqVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.aop.annotation.LogAnnotation;
import com.ygkj.water.project.service.WarningRuleService;
import com.ygkj.water.project.service.WarningUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wgf
 * @date 2021/10/22 15:08
 * @Description:
 */
@Api(tags = "预警规则配置")
@RestController
@RequestMapping("/warningRule")
public class WarningRuleController {

    @Autowired
    private WarningRuleService warningRuleService;
    @Autowired
    private WarningUserService warningUserService;

    @ApiOperation(value = "新增预警规则配置")
    @PostMapping("/insert")
    @LogAnnotation(title = "风险预警-规则配置管理",action = "新增预警规则配置")
    public CommonResult insert(WarningRule warningRule) {
        //判断预警规则类型是否已经存在
        Integer warningrule = warningRule.getWarningrule();
        if (warningrule == null){
            return CommonResult.failed("预警规则类型不能为空");
        }
        if(warningrule < 12 && warningrule != 8){
            WarningRule one = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", warningrule));
            if (one != null) {
                return CommonResult.failed("预警规则类型已经存在，你可以修改原数据!");
            }
        } else {
            if (warningrule == 13){
                Double maximum = warningRule.getMaximum();
                Double minimum = warningRule.getMinimum();
                if (maximum == null || minimum == null){
                    return CommonResult.failed("上限值或者下限值不能为空");
                }else {
                    if (maximum < minimum){
                        return CommonResult.failed("上限值不能小于下限值");
                    }
                }
            }
        }
        if (warningRuleService.save(warningRule)) {
            return CommonResult.success();
        }
        return CommonResult.failed("新增失败");
    }

    @ApiOperation(value = "删除预警规则配置")
    @GetMapping("/delete")
    @LogAnnotation(title = "风险预警-规则配置管理",action = "删除预警规则配置")
    public CommonResult delete(String id) {
        if (warningRuleService.removeById(id)) {
            return CommonResult.success();
        }
        return CommonResult.failed("删除失败");
    }

    @ApiOperation(value = "更新预警规则配置")
    @PostMapping("/update")
    @LogAnnotation(title = "风险预警-规则配置管理",action = "更新预警规则配置")
    public CommonResult update(WarningRule warningRule) {
        //查看除了自己之外是否有现在修改的类型
        Integer warningrule = warningRule.getWarningrule();
        if(warningrule < 12 && warningrule != 8){
            WarningRule one = warningRuleService.getOne(new QueryWrapper<WarningRule>().eq("warningRule", warningrule).ne("id", warningRule.getId()));
            if (one != null) {
                return CommonResult.failed("预警规则类型已经存在，请你重新选择类型!");
            }
        }

        //更新报错走failed
        try{
            //值没发生改变，没有更新，没有发生报错,相当于更新到原来的值，直接返回成功
            warningRuleService.updateById(warningRule);
            return CommonResult.success();
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed("更新失败");
    }

    @ApiOperation(value = "根据id查询预警规则配置记录")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
        WarningRule warningRule = warningRuleService.getById(id);
        String sendcode = warningRule.getSendcode();
        String bindcode = warningRule.getBindcode();
        if (StringUtils.isNotBlank(sendcode)) {
            String[] split = sendcode.split(",");
            List<WarningUser> warningUsers = warningUserService.listByIds(Arrays.asList(split));
            warningRule.setUsers(warningUsers);
        }
        if (StringUtils.isNotBlank(bindcode)) {
            String[] split = bindcode.split(",");
            Integer warningrule = warningRule.getWarningrule();
            List<JSONObject> jsonObjects;
            if (warningrule <= 7) {
                //绑定海塘
                jsonObjects = warningRuleService.getSeawallByIds(Arrays.asList(split));
            } else if (warningrule <=13){
                //绑定测站点
                jsonObjects = warningRuleService.getStationByIds(Arrays.asList(split));
            }else {
                jsonObjects = warningRuleService.getProjectByIds(Arrays.asList(split));
            }
            warningRule.setBindList(jsonObjects);
        }
        return CommonResult.success(warningRule);
    }

    @ApiOperation(value = "预警规则配置列表")
    @PostMapping("/list")
    @LogAnnotation(title = "风险预警-规则配置管理",action = "预警规则列表")
    public CommonResult list(WarningRule warningRule) {
        warningRule.page();
        QueryWrapper<WarningRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(warningRule.getWarningrule() != null, "warningRule", warningRule.getWarningrule());
        queryWrapper.eq(warningRule.getRuletype() != null, "ruleType", warningRule.getRuletype());
        queryWrapper.eq(warningRule.getIsopen() != null, "isopen", warningRule.getIsopen());
        queryWrapper.orderByDesc("tm");
        return CommonResult.success(PageUtils.getPageVO(warningRuleService.list(queryWrapper)));
    }

    @ApiOperation(value = "预警规则导出")
    @PostMapping("/exportList")
    public void exportRivernet(WarningRule warningRule, HttpServletResponse response) {
        QueryWrapper<WarningRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(warningRule.getWarningrule() != null, "warningRule", warningRule.getWarningrule());
        queryWrapper.eq(warningRule.getIsopen() != null, "isopen", warningRule.getIsopen());
        queryWrapper.orderByDesc("tm");
        List<WarningRule> list = warningRuleService.list(queryWrapper);
        for (WarningRule rule : list) {
            //预警规则1.安全鉴定超期，2.汛前检查超期，3.年度总结超期，4.巡查信息异常，5.工程隐患，6.海塘实时报警，
            // 7.海塘预报预警，8.时段雨量报警，9.河道水情报警，10.水库水情报警，11.潮位报警
            Integer warningrule = rule.getWarningrule();
            //报警启闭（1.开启、2关闭）
            Integer isopen = rule.getIsopen();
            if (isopen != null) {
                if (isopen == 1) {
                    rule.setIsOpenString("开启");
                } else {
                    rule.setIsOpenString("关闭");
                }
            }
            if (warningrule != null) {
                switch (warningrule) {
                    case 1:
                        rule.setWarningRuleString("安全鉴定超期");
                        break;
                    case 2:
                        rule.setWarningRuleString("汛前检查超期");
                        break;
                    case 3:
                        rule.setWarningRuleString("年度总结超期");
                        break;
                    case 4:
                        rule.setWarningRuleString("巡查信息异常");
                        break;
                    case 5:
                        rule.setWarningRuleString("工程隐患");
                        break;
                    case 6:
                        rule.setWarningRuleString("海塘实时报警");
                        break;
                    case 7:
                        rule.setWarningRuleString("海塘预报预警");
                        break;
                    case 8:
                        rule.setWarningRuleString("时段雨量报警");
                        break;
                    case 9:
                        rule.setWarningRuleString("河道水情报警");
                        break;
                    case 10:
                        rule.setWarningRuleString("水库水情报警");
                        break;
                    case 12:
                        rule.setWarningRuleString("测站关联管理");
                        break;
                    case 13:
                        rule.setWarningRuleString("测站限值管理");
                        break;
                    case 14:
                        rule.setWarningRuleString("预案资料提醒");
                        break;
                    case 15:
                        rule.setWarningRuleString("日常检查提醒");
                        break;
                    case 16:
                        rule.setWarningRuleString("汛后检查提醒");
                        break;
                    case 17:
                        rule.setWarningRuleString("年度检查提醒");
                        break;
                    case 18:
                        rule.setWarningRuleString("汛前检查提醒");
                        break;
                    default:
                }
            }
        }
        ExportUtils.exportExcel(list, "预警规则",
                new String[]{"预警规则", "站点数目", "发送人数", "报警启闭"},
                new String[]{"warningRuleString", "bindcount", "sendcount", "isOpenString"},
                response, null, Collections.emptyMap());
    }

    @ApiOperation(value = "获取测站列表")
    @PostMapping("/stList")
    public CommonResult list(AttStBase attStBase) {
        return CommonResult.success(warningRuleService.getStList(attStBase));
    }

    @ApiOperation(value = "获取工程列表")
    @PostMapping("/projectList")
    public CommonResult projectList(String projectName) {
        return CommonResult.success(warningRuleService.projectList(projectName));
    }

    @ApiOperation(value = "测站关联管理/测站限值管理的列表")
    @PostMapping("/listRuleSt")
    public CommonResult listRuleSt(RuleStReqVo reqVo) {
        return CommonResult.success(warningRuleService.listRuleSt(reqVo));
    }
}
