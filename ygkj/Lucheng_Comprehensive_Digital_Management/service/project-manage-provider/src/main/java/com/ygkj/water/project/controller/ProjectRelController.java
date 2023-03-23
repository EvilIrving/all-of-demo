package com.ygkj.water.project.controller;

import com.alibaba.excel.EasyExcel;
import com.ygkj.project.model.BhDict;
import com.ygkj.project.model.BhLgTermiteCheck;
import com.ygkj.project.model.FloodRiskCenter;
import com.ygkj.project.model.SlProjStR;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjectRelService;
import com.ygkj.water.project.vo.MonitoringStationResVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("projectRel")
@Api(tags = "工程关系")
public class ProjectRelController {

    @Autowired
    private ProjectRelService projectRelService;

    @PostMapping("listSt")
    @ApiOperation(value = "工程关联测站列表",response = SlProjStResVo.class)
    public CommonResult listSt(ProRelStVo stVo){
        return CommonResult.success(projectRelService.listSt(stVo));
    }

    @PostMapping("pagePatrol")
    @ApiOperation(value = "分页查询工程巡查列表",response = ProjectPatrolResVo.class)
    public CommonResult pagePatrol(ProjectPatrolReqVo reqVo){
        return CommonResult.success(projectRelService.pagePatrol(reqVo));
    }

    @PostMapping("exportPagePatrol")
    @ApiOperation(value = "分页查询工程巡查列表")
    public void exportPagePatrol(ProjectPatrolReqVo reqVo,HttpServletResponse response){
        projectRelService.exportPagePatrol(reqVo,response);
    }

    @PostMapping("pageDanger")
    @ApiOperation(value = "分页查询工程隐患列表",response = ProjectDangerResVo.class)
    public CommonResult pageDanger(ProjectDangerReqVo reqVo){
        return CommonResult.success(projectRelService.pageDanger(reqVo));
    }

    @PostMapping("exportPageDanger")
    @ApiOperation(value = "导出分页查询工程隐患列表")
    public void exportPageDanger(ProjectDangerReqVo reqVo,HttpServletResponse response){
        projectRelService.exportPageDanger(reqVo,response);
    }

    @PostMapping("listDict")
    @ApiOperation(value = "字典列表",response = BhDict.class)
    public CommonResult listDict(BhDict bhDict){
        return CommonResult.success(projectRelService.listDict(bhDict));
    }

    @PostMapping("pageGreatDanger")
    @ApiOperation(value = "分页查询工程重大历史险情",response = ProjectGreatDangerResVo.class)
    public CommonResult pagePatrol(ProjectGreatDangerReqVo reqVo){
        return CommonResult.success(projectRelService.pageGreatDanger(reqVo));
    }

    @PostMapping("listMs")
    @ApiOperation(value = "分页或者列表查询监测站点",response = MonitoringStationResVo.class)
    public CommonResult listMs(MonitoringStationReqVo reqVo){
        return CommonResult.success(projectRelService.listMs(reqVo));
    }

    @PostMapping("exportMs")
    @ApiOperation(value = "导出监测站点",response = MonitoringStationResVo.class)
    public void exportMs(MonitoringStationReqVo reqVo, HttpServletResponse response){
        PageVO<MonitoringStationResVo> page = projectRelService.listMs(reqVo);
        List<MonitoringStationResVo> list = page.getList();
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("监测站点", "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            response.addHeader("Access-Control-Expose-Headers","Content-Disposition");
            EasyExcel.write(response.getOutputStream(),MonitoringStationResVo.class).sheet("监测站点").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("selectDuty/{projectCode}")
    @ApiOperation(value = "值班人员查询",response = TwoDutyResVo.class)
    public CommonResult selectDuty(@PathVariable("projectCode")String projectCode){
        return CommonResult.success(projectRelService.selectDuty(projectCode));
    }

    @GetMapping("selectManageInfo")
    @ApiOperation(value = "管理信息",response = ManageInfoResVo.class)
    public CommonResult selectManageInfo(String projectCode){
        return CommonResult.success(projectRelService.selectManageInfo(projectCode));
    }

    @GetMapping("selectRuntimeWarn")
    @ApiOperation(value = "运行报警",response = RuntimeWarnResVo.class)
    public CommonResult selectRuntimeWarn(String projectCode){
        return CommonResult.success(projectRelService.selectRuntimeWarn(projectCode));
    }

    @PostMapping("updateProjRel")
    @ApiOperation(value = "编辑工程站点绑定关系")
    public CommonResult updateProjRel(@RequestBody BatchSlProjStReqVo reqVo){
        projectRelService.updateProjRel(reqVo);
        return CommonResult.success("");
    }

    @PostMapping("delProjRel")
    @ApiOperation(value = "解除工程绑定关系")
    public CommonResult delProjRel(SlProjStR slProjStR){
        projectRelService.delProjRel(slProjStR);
        return CommonResult.success("");
    }

    @PostMapping("pageProjRel")
    @ApiOperation(value = "分页查询工程绑定(全部)的站点",response = SlProjStRResVo.class)
    public CommonResult pageProjRel(SlProjStReqVo reqVo){
        return CommonResult.success(projectRelService.pageProjRel(reqVo));
    }

    @PostMapping("updateMainSt")
    @ApiOperation(value = "设置(取消)主站")
    public CommonResult updateMainSt(SlProjStR slProjStR){
        projectRelService.updateMainSt(slProjStR);
        return CommonResult.success("");
    }

    @GetMapping("countRel/{projectCode}")
    @ApiOperation(value = "查询各个站点数量",response = CountRelResVo.class)
    public CommonResult countRel(@PathVariable("projectCode")String projectCode){
        return CommonResult.success(projectRelService.countRel(projectCode));
    }

    @PostMapping("waterLevelWarn")
    @ApiOperation(value = "工程超警",response = WaterLevelWarnResVo.class)
    public CommonResult waterLevelWarn(WaterLevelWarnReqVo reqVo){
        return CommonResult.success(projectRelService.waterLevelWarn(reqVo));
    }

    @GetMapping("waterLevelWarnCount")
    @ApiOperation(value = "工程超警统计")
    public CommonResult waterLevelWarnCount(){
        return CommonResult.success(projectRelService.waterLevelWarnCount());
    }

    @GetMapping("listRuntimeWarn")
    @ApiOperation(value = "工程运行统计",response = RuntimeWarnAllProjectResVo.class)
    public CommonResult listRuntimeWarn(RuntimeWarnReqVo reqVo){
        return CommonResult.success(projectRelService.listRuntimeWarn(reqVo));
    }

    @PostMapping("pageBeforeFlood")
    @ApiOperation(value = "汛前检查",response = BeforeFloodResVo.class)
    public CommonResult pageBeforeFlood(CheckReqVo reqVo){
        return CommonResult.success(projectRelService.pageBeforeFlood(reqVo));
    }

    @PostMapping("pageYearReport")
    @ApiOperation(value = "年度检查",response = BhLgMpAcResVo.class)
    public CommonResult pageYearReport(CheckReqVo reqVo){
        return CommonResult.success(projectRelService.pageYearReport(reqVo));
    }

    @PostMapping("pageTermiteCheck")
    @ApiOperation(value = "白蚁检查",response = BhLgTermiteCheck.class)
    public CommonResult pageTermiteCheck(CheckReqVo reqVo){
        return CommonResult.success(projectRelService.pageTermiteCheck(reqVo));
    }

    @PostMapping("floodRiskCenter")
    @ApiOperation(value = "防汛风险C位",response = FloodRiskCenter.class)
    public CommonResult floodRiskCenter(){
        return CommonResult.success(projectRelService.floodRiskCenter());
    }

    @PostMapping("getProjList")
    public CommonResult getProjList(List<String> codeList){
            return CommonResult.success(projectRelService.getProjList(codeList));
    }
}
