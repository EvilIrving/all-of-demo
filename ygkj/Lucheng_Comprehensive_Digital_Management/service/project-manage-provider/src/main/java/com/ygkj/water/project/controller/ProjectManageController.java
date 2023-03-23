package com.ygkj.water.project.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.PointEntity;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.project.ProjectManageControllerApi;
import com.ygkj.project.model.AttProjBuildContent;
import com.ygkj.project.model.AttProjLink;
import com.ygkj.project.vo.request.*;
import com.ygkj.utils.DateUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/projectManage")
public class ProjectManageController implements ProjectManageControllerApi {

    @Autowired
    private ProjectManageService projectManageService;
    @Autowired
    private ProjectStatusService projectStatusService;
    @Autowired
    private RsvrService rsvrService;
    @Autowired
    private WagaService wagaService;
    @Autowired
    private FloodResService floodResService;
    @Autowired
    private AttReservoirArchivesService attReservoirArchivesService;


    @PostMapping("/projects")
    @Override
    public CommonResult projects(ProjectManageQueryVo projectManageQueryVo) {
        return CommonResult.success(projectManageService.projectList(projectManageQueryVo));
    }

    @PostMapping("/stats4ProjectList")
    @Override
    public CommonResult stats4ProjectList(ProjectManageQueryVo projectManageQueryVo) {
        return CommonResult.success(projectManageService.stats4ProjectList(projectManageQueryVo));
    }

    @GetMapping("/projectCameras/{projectCode}")
    @Override
    public CommonResult projectCameras(@PathVariable("projectCode") String projectCode, @RequestParam(value = "cameraName", required = false) String cameraName) {
        CameraQueryVo cameraQueryVo = new CameraQueryVo();
        cameraQueryVo.setProjectCode(projectCode);
        if (StringUtils.isNotBlank(cameraName)) {
            cameraQueryVo.setCameraName(cameraName);
        }
        return CommonResult.success(projectManageService.projectCameras(cameraQueryVo));
    }

    @PostMapping("/projectChargePerson")
    @Override
    public CommonResult projectChargePerson(String projectCode, String projectType) {
        ProjectManageQueryVo vo = new ProjectManageQueryVo();
        vo.setCode(projectCode);
        vo.setType(projectType);
        return CommonResult.success(projectManageService.projectChargePerson(vo));
    }

    @GetMapping("/cameraStatistics")
    @Override
    public CommonResult cameraStatistics(Integer index) {
        if (index == 1) {
            return CommonResult.success(projectManageService.cameraStatistics());
        } else {
            return CommonResult.success(projectManageService.cameraOutLineStatistics());
        }
    }

    @GetMapping("/outLineCameras")
    @Override
    public CommonResult outLineCameras() {
        return CommonResult.success(projectManageService.outLineCameras());
    }

    @GetMapping("/outLineCamerasOrderList")
    @Override
    public CommonResult outLineCamerasOrderList() {
        return CommonResult.success(projectManageService.outLineCamerasOrderList());
    }

    @GetMapping("/outLineCamerasCountByBasin")
    @Override
    public CommonResult outLineCamerasCountByBasin() {
        return CommonResult.success(projectManageService.outLineCamerasCountByBasin());
    }

    @GetMapping("/outLineCamerasCountByAdcd")
    @Override
    public CommonResult outLineCamerasCountByAdcd() {
        return CommonResult.success(projectManageService.outLineCamerasCountByAdcd());
    }

    @GetMapping("/outLineCamerasCountByProjectType")
    @Override
    public CommonResult outLineCamerasCountByProjectType() {
        return CommonResult.success(projectManageService.outLineCamerasCountByProjectType());
    }

    @GetMapping("/countCamerasByStatus")
    @Override
    public CommonResult countCamerasByStatus() {
        return CommonResult.success(projectManageService.cameraStatisticByStatus());
    }

    @GetMapping("/reservoirNewestWaterLevel/{projectCode}")
    @Override
    public CommonResult reservoirNewestWaterLevel(@PathVariable String projectCode) {
        return CommonResult.success(floodResService.reservoirWaterLevel(projectCode));
    }

    // 获取工程对应建设内容信息
    @Override
    @GetMapping("/proBuildContents")
    public CommonResult<List<AttProjBuildContent>> proBuildContents(@RequestParam("projCode") String projCode, @RequestParam(value = "type", required = false) String type) {

        return CommonResult.success(projectManageService.proBuildContents(projCode, type));
    }

    // 获取工程对应外链信息
    @Override
    @GetMapping("/proLinks/{projCode}")
    public CommonResult<List<AttProjLink>> proLinks(@PathVariable String projCode) {

        return CommonResult.success(projectManageService.proLinks(projCode));
    }

    @GetMapping("projectOverview")
    @Override
    public CommonResult projectOverview() {
        return CommonResult.success(projectManageService.projectOverview());
    }

    @Override
    @GetMapping("/projectInfoByCode/{projCode}")
    public CommonResult projectInfoByCode(@PathVariable String projCode) {

        return CommonResult.success(projectManageService.projectInfoByCode(projCode));
    }

    @Override
    @GetMapping("/getProjectQrCode")
    public void getProjectQrCode(@RequestParam("httpUrl") String httpUrl, HttpServletResponse response) throws IOException {

        projectManageService.getQrCodeByProjCode(httpUrl, response);
    }

    @PostMapping("/projectInScope")
    @Override
    public CommonResult projectInScope(@RequestBody List<PointEntity> list) {
        return CommonResult.success(projectManageService.projectInScope(list));
    }

    @PostMapping("/projectInCircle")
    @Override
    public CommonResult projectInCircle(@RequestBody CircleReqVo reqVo) {
        return CommonResult.success(projectManageService.projectInCircle(reqVo));
    }

    @GetMapping("countProjTypeNumByAdcdBasin")
    @Override
    public CommonResult countProjTypeNumByAdcdBasin(@RequestParam(value = "adcd", required = false) String adcd,
                                                    @RequestParam(value = "basin", required = false) String basin) {

        return CommonResult.success(projectManageService.countProjTypeNumByAdcdBasin(adcd, basin));
    }

    @GetMapping("/dikeAndSeawallLengthCount")
    @Override
    public CommonResult dikeAndSeawallLengthCount() {
        return CommonResult.success(projectManageService.dikeAndSeawallLengthCount());
    }

    @GetMapping("/projectCount")
    @Override
    public CommonResult projectCount(@RequestParam("type") String type, @RequestParam(value = "stType", required = false) String stType) {
        return CommonResult.success(projectManageService.projectCount(type, stType));
    }

    @GetMapping("projStatStatistic")
    @Override
    public CommonResult projStatStatistic(@RequestParam("type") String type,
                                          @RequestParam("dimension") String dimension) {
        return CommonResult.success(projectManageService.projStatStatistic(type, dimension));
    }

    @Override
    @GetMapping("/reservoirArchivesCount")
    public CommonResult reservoirArchivesCount() {
        return CommonResult.success(attReservoirArchivesService.reservoirArchivesCount());
    }

    @Override
    @GetMapping("/attStBaseCountByType")
    public CommonResult attStBaseCountByType(@RequestParam("type") String type,
                                             @RequestParam(value = "stType", required = false) String stType,
                                             @RequestParam(value = "isWrp", required = false) Boolean isWrp) {
        return CommonResult.success(projectManageService.attStBaseCountByType(type, stType, isWrp));
    }


    @PostMapping("/rsvrNewestWaterLevel")
    @Override
    public CommonResult rsvrNewestWaterLevel(@RequestBody RsvrStationWaterReqVo reqVo) {
        return CommonResult.success(rsvrService.rsvrNewestWaterLevel(reqVo));
    }

    @PostMapping("rsvrWaterLevel")
    @Override
    public CommonResult rsvrWaterLevel(@RequestBody RsvrStationWaterReqVo reqVo) {
        return CommonResult.success(rsvrService.rsvrWaterLevel(reqVo));
    }

    @PostMapping("wagaWaterLevel")
    @Override
    public CommonResult wagaWaterLevel(@RequestBody WagaStationWaterReqVo reqVo) {
        return CommonResult.success(wagaService.wagaWaterLevel(reqVo));
    }

    @GetMapping("dilapProjList")
    @Override
    public CommonResult dilapProjList(DilapProjReqVo reqVo) {
        return CommonResult.success(projectManageService.dilapProjList(reqVo));
    }

    @GetMapping("projRiskList")
    @Override
    public CommonResult projRiskList(ProjRiskReqVo reqVo) {
        return CommonResult.success(projectManageService.projRiskList(reqVo));
    }

    @GetMapping("seawallStatsForBigScreen")
    @Override
    public CommonResult seawallStatsForBigScreen() {
        return CommonResult.success(projectManageService.seawallStatsForBigScreen());
    }

    @GetMapping("rsvrReplayStats")
    @Override
    public CommonResult rsvrReplayStats(RsvrReplayReqVo reqVo) {
        return CommonResult.success(rsvrService.rsvrReplayStats(reqVo));
    }

    @GetMapping("wagaReplayStats")
    public CommonResult wagaReplayStats(WagaReplayReqVo reqVo) {
        return CommonResult.success(wagaService.wagaReplayStats(reqVo));
    }

    @GetMapping("hydrProjStaDiagram")
    @Override
    public CommonResult hydrProjStaDiagram(HydrDiagReqVo reqVo) {
        return CommonResult.success(projectStatusService.hydrProjStaDiagram(reqVo));
    }

    @GetMapping("hydrProjStaDiagramStatistic")
    @Override
    public CommonResult hydrProjStaDiagramStatistic(Integer hours) {
        return CommonResult.success(projectStatusService.hydrProjStaDiagramStatistic(hours));
    }

    @GetMapping("/countProjectNumber")
    @Override
    public CommonResult countProjectNumber() {
        return CommonResult.success(projectManageService.haha());
    }

    @GetMapping("/rsvrProjDrainageStats")
    @Override
    public CommonResult rsvrProjDrainageStats(@RequestParam(value = "dataTime", required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dataTime) {
        return CommonResult.success(rsvrService.rsvrProjDrainageStats(dataTime));
    }

    @GetMapping("/getRsvrDrillByTime")
    @Override
    public CommonResult getRsvrDrillByTime(@RequestParam(value = "dataTime") String dataTime) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return CommonResult.success(rsvrService.getRsvrDrillByTime(DateUtils.date2LocalDateTime(sdf.parse(dataTime))));
    }

    @GetMapping("getAllProjType")
    @Override
    public CommonResult getAllProjType() {
        return CommonResult.success(projectManageService.getAllProjType());
    }

    @GetMapping("saveChooseProject")
    @Override
    public CommonResult saveChooseProject(String dataStr) {
        projectManageService.saveChooseProject(dataStr);
        return CommonResult.success();
    }

    @GetMapping("getChooseProject")
    @Override
    public CommonResult getChooseProject() {
        return CommonResult.success(projectManageService.getChooseProject());
    }


    @Override
    @GetMapping("/countProjectNum")
    public CommonResult countProjectNum(String type) {
        return CommonResult.success(projectManageService.countProjectNum(type));
    }

    @PostMapping("/getDynamicToken")
    @ApiOperation(value = "获取协同平台动态token，有效期15分钟")
    public CommonResult getDynamicToken() {
        String s = HttpUtil.get("https://sk.zjwater.com/out/service/getDynamicToken");
        JSONObject jsonObject = JSONObject.parseObject(s);
        if(null!=jsonObject && jsonObject.containsKey("token")){
            return CommonResult.success(jsonObject.getString("token"));
        }else{
            return CommonResult.failed("获取token失败");
        }
    }

    @PostMapping("/getHikvisionTree")
    @ApiOperation(value = "摄像头行政区划树")
    public CommonResult getHikvisionTree() {
        return CommonResult.success(projectManageService.getHikvisionTree());
    }
}
