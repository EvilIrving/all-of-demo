package com.ygkj.water.project.controller;

import com.ygkj.project.ProjectMaintenanceControllerApi;
import com.ygkj.project.model.WarningTemplate;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.ProjectManageService;
import com.ygkj.water.project.utils.ExportUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/projectMaintenance")
public class ProjectMaintenanceController implements ProjectMaintenanceControllerApi {

    @Autowired
    private ProjectManageService projectManageService;


    @PostMapping("/projectCameras")
    @Override
    public CommonResult projectCameras(ProjectCameraPageQueryVo queryVo) {
        return CommonResult.success(projectManageService.projectCameras(queryVo));
    }

    @PutMapping("/cameraAdd")
    @Override
    public CommonResult cameraAdd(CameraAddVo cameraAddVo) {
        if (projectManageService.addCamera(cameraAddVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/cameraUpdate")
    @Override
    public CommonResult cameraUpdate(CameraAddVo cameraAddVo) {
        if (projectManageService.updateCameras(cameraAddVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }


    @DeleteMapping("/cameraDelete")
    @Override
    public CommonResult cameraDelete(String cameraIndexCodes) {
        if (projectManageService.batchDeleteCameras(cameraIndexCodes)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @GetMapping("/countCamerasByProjectType")
    @Override
    public CommonResult countCamerasByProjectType() {
        return CommonResult.success(projectManageService.countCamerasByProjectType());
    }

    @GetMapping("/countCamerasByAdcd")
    @Override
    public CommonResult countCamerasByAdcd() {
        return CommonResult.success(projectManageService.countCamerasByAdcd());
    }

    // 报警模板查询接口
    @PostMapping("/WarningTemplatePageQuery")
    @Override
    public CommonResult WarningTemplatePageQuery(WarningTemplateQueryVo queryVo) {
        return CommonResult.success(projectManageService.selectWarningTemplate(queryVo));
    }

    // 报警模板新增
    @PostMapping("/addWarningTemplate")
    @Override
    public CommonResult addWarningTemplate(WarningTemplateAddVo vo) {
        if (projectManageService.addWarningTemplate(vo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    // 报警模板修改
    @PutMapping("/updateWarningTemplate")
    @Override
    public CommonResult updateWarningTemplate(WarningTemplateAddVo vo) {
        if (projectManageService.updateWarningTemplate(vo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    // 报警模板删除，支持批量
    @DeleteMapping("/warningTypeDelete")
    @Override
    public CommonResult warningTypeDelete(String ids) {
        if (projectManageService.batchDeleteWarningTemplate(ids)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/warningConfigs")
    @Override
    public CommonResult warningConfigs(WarningConfigPageQueryVo warningConfigPageQueryVo) {
        return CommonResult.success(projectManageService.warningConfigs(warningConfigPageQueryVo));
    }

    @PutMapping("/addWarningConfig")
    @Override
    public CommonResult addWarningConfig(WarningConfigAddVo warningConfigAddVo) {
        if (projectManageService.addWarningConfig(warningConfigAddVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/updateWarningConfig")
    @Override
    public CommonResult updateWarningConfig(WarningConfigAddVo warningConfigAddVo) {
        if (projectManageService.updateWarningConfig(warningConfigAddVo)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @DeleteMapping("/deleteWarningConfig")
    @Override
    public CommonResult deleteWarningConfig(String ids) {
        if (projectManageService.batchDeleteWarningConfig(ids)) {
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/warningRecords")
    @Override
    public CommonResult warningRecords(WarningRecordPageQueryVo queryVo) {
        return CommonResult.success(projectManageService.selectWarningRecord(queryVo));
    }

    @Override
    @PostMapping("/exportTemplateList")
    public void exportTemplateList(WarningTemplateQueryVo queryVo, HttpServletResponse response) {
        List<WarningTemplate> list = projectManageService.listWarningTemplate(queryVo);
        ExportUtils.exportExcel(list,
                "模板列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"报警类型", "模板名称", "模板内容"},
                new String[]{"warningType", "templateName", "templateContent"},
                response);
    }

    // 报警配置-导出
    @Override
    @PostMapping("/exportwarningConfigs")
    public void exportwarningConfigs(WarningConfigQueryVo vo, HttpServletResponse response) {
        vo.setFetchAll(true);
        PageVO<RainWarningConfig> pageVO = projectManageService.warningConfigQuery(vo);
        List<RainWarningConfig> list = pageVO.getList();

        ExportUtils.exportExcel2(list,
                "报警配置列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"报警类型", "站点名称", "模板名称", "模板内容", "报警方式", "降雨历时", "降雨阈值", "报警接收人", "启用状态（0停用，1启用）",
                        "汛限水位", "防洪高水位", "警戒水位", "保证水位", "蓝色警戒水位", "黄色警戒水位", "橙色警戒水位", "红色警戒水位", "报警条件", "流量阈值m³/s", "平均蓄水量m³"},
                new String[]{"warningType", "stName", "templateName", "templateContent", "pattren", "rainfallPeriod", "threshold", "userNames", "openStatus",
                        "limitLevel", "floodLevel", "warningLevel", "guaranteeLevel", "blueLevel", "yellowLevel", "orangeLevel", "redLevel",
                        "warnCondition", "flowThreshold", "avgCapacity"},
                response, RainWarningConfig.class);
    }

    // 报警配置-查询
    @Override
    @PostMapping("/warningConfigQuery")
    public CommonResult<PageVO<RainWarningConfig>> warningConfigQuery(WarningConfigQueryVo vo) {

        return CommonResult.success(projectManageService.warningConfigQuery(vo));
    }

    // 报警配置-查询测站信息(降雨)
    @Override
    @PostMapping("/staInfos")
    public CommonResult<List<WarnConfigStaInfo>> staInfos(StaInfoQueryVo vo,String stCodes) {

        return CommonResult.success(projectManageService.staInfos(vo,stCodes));
    }

    // 报警配置-更新插入测站降雨历时对应阈值-返回配置id
    @Override
    @PostMapping("/warnThresholdOperate")
    public CommonResult warnThresholdOperate(WarnThresholdOperateVo vo) {

        if (null == vo || StringUtils.isEmpty(vo.getStCodes())) {
            return CommonResult.failed("缺少必要参数");
        }
        return CommonResult.success(projectManageService.warnThresholdOperate(vo));
    }

    // 获取人员信息
    @Override
    @GetMapping("/userInfos")
    public CommonResult<List<DeptUserInfo>> userInfos() {
        return CommonResult.success(projectManageService.userInfos());
    }

    // 报警配置-新增
    @Override
    @PostMapping("/warningConfigAdd")
    public CommonResult warningConfigAdd(WarningConfigAddReqVo vo) {

        return CommonResult.success(projectManageService.warningConfigAdd(vo));
    }

    // 报警配置-查询测站信息(水位)
    @Override
    @PostMapping("/waterStaInfos")
    public CommonResult<List<WaterStaInfoRespVo>> waterStaInfos(StaInfoQueryVo vo) {

        return CommonResult.success(projectManageService.waterStaInfos(vo));
    }

    // 报警配置-水位更新插入测站类型对应阈值类型-返回配置id
    @Override
    @PostMapping("/waterWarnThresholdOperate")
    public CommonResult waterWarnThresholdOperate(WaterWarnThresholdOperateVo vo) {

        return CommonResult.success(projectManageService.waterWarnThresholdOperate(vo));
    }

    // 报警配置-查询测站信息(流量)
    @Override
    @PostMapping("/flowStaInfos")
    public CommonResult<List<FlowStaInfoRespVo>> flowStaInfos(StaInfoQueryVo vo) {

        return CommonResult.success(projectManageService.flowStaInfos(vo));
    }

    // 报警配置-流量更新插入报警条件对应流量阈值-返回配置id
    @Override
    @PostMapping("/flowWarnThresholdOperate")
    public CommonResult flowWarnThresholdOperate(FlowWarnThresholdOperateVo vo) {

        return CommonResult.success(projectManageService.flowWarnThresholdOperate(vo));
    }

    // 报警配置-查询测站信息(蓄水量)
    @Override
    @PostMapping("/capacityStaInfos")
    public CommonResult<List<CapacityStaInfoRespVo>> capacityStaInfos(StaInfoQueryVo vo) {

        return CommonResult.success(projectManageService.capacityStaInfos(vo));
    }

    // 报警配置-流量更新插入报警条件对应流量阈值-返回配置id
    @Override
    @PostMapping("/capacityWarnThresholdOperate")
    public CommonResult capacityWarnThresholdOperate(CapacityWarnThresholdOperateVo vo) {

        return CommonResult.success(projectManageService.capacityWarnThresholdOperate(vo));
    }

    // 报警配置-修改（根据测站id）
    @Override
    @PostMapping("/warningConfigUpd")
    public CommonResult warningConfigUpd(WarnConfigUdpReqVo vo) {

        if (StringUtils.isEmpty(vo.getStCode()) || StringUtils.isEmpty(vo.getConfigId())) {
            return CommonResult.failed("参数缺失");
        }
        return CommonResult.success(projectManageService.warningConfigUpd(vo));
    }

    // 报警配置-删除（根据测站id）
    @Override
    @PostMapping("/warningConfigDel")
    public CommonResult warningConfigDel(String configIds, String stCodes) {

        if (StringUtils.isEmpty(stCodes) || StringUtils.isEmpty(configIds)) {
            return CommonResult.failed("参数缺失");
        }
        return CommonResult.success(projectManageService.warningConfigDel(configIds, stCodes));
    }

    // 报警分析-查询
    @Override
    @PostMapping("/warnAnalysisQuery")
    public CommonResult<PageVO<WarnAnalyseRespVo>> warnAnalysisQuery(WarningAnalyseQueryVo vo) {

        return CommonResult.success(projectManageService.warnAnalysisQuery(vo));
    }

    // 报警分析-导出
    @Override
    @PostMapping("/exportWarningAnalyses")
    public void exportWarningAnalyses(WarningAnalyseQueryVo vo, HttpServletResponse response) {
        vo.setFetchAll(true);
        PageVO<WarnAnalyseRespVo> pageVO = projectManageService.warnAnalysisQuery(vo);

        ExportUtils.exportExcel2(pageVO.getList(),
                "报警分析列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"报警时间", "报警类型", "站点名称", "报警方式", "报警内容", "报警接收人", "转发状态（0待转，1转发）",
                        "降雨历时", "降雨阈值", "降雨量", "水位", "汛限水位", "防洪高水位", "警戒水位", "保证水位", "蓝色警戒水位",
                        "黄色警戒水位", "橙色警戒水位", "红色警戒水位", "报警条件", "流量阈值m³/s", "流量", "平均蓄水量m³", "蓄水量"},
                new String[]{"warningTime", "warningType", "stName", "pattren", "warningContent", "userNames", "forwordStatus",
                        "rainfallPeriod", "threshold", "rainfall", "waterLevel", "limitLevel", "floodLevel", "warningLevel", "guaranteeLevel", "blueLevel",
                         "yellowLevel", "orangeLevel", "redLevel", "warnCondition", "flowThreshold", "flow", "avgCapacity", "capacity"},
                response, WarnAnalyseRespVo.class);
    }

    // 报警分析-转发
    @Override
    @PostMapping("/forwordWarnAnalyse")
    public CommonResult forwordWarnAnalyse(ForwordWarnAnalyseReqVo vo) {

        if (null == vo) {
            return CommonResult.failed("参数缺失");
        }
        return CommonResult.success(projectManageService.forwordWarnAnalyse(vo));
    }


    /**
     * 报警管理导出
     * @param vo
     * @param response
     */
    @Override
    @PostMapping("/exportWarningRecords")
    public void exportWarningRecords(WarningRecordPageQueryVo vo, HttpServletResponse response) {
        vo.setFetchAll(true);
        PageVO pageVO = projectManageService.selectWarningRecord(vo);
        List<WarningRecordResVo> list = pageVO.getList();

        ExportUtils.exportExcel(list,
                "报警管理列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"报警时间","报警类型","摄像机名称","所属工程","工程类型","报警方式","报警内容","报警接收人"},
                new String[]{"warningTime","warningType","equipmentName","projectName","projectType","pattren","templateContent","person"},
                response, WarningRecordResVo.class);
    }
}
