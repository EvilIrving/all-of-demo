package com.ygkj.water.project.controller;

import com.ygkj.project.CameraOpsControllerApi;
import com.ygkj.project.model.AttCameraOps;
import com.ygkj.project.model.HikvisionCameraRecord;
import com.ygkj.project.vo.request.CamBindObjReqVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.request.CameraOpsQueryReqVo;
import com.ygkj.project.vo.response.CountCameraStatus;
import com.ygkj.project.vo.response.HikCameraResVo;
import com.ygkj.utils.ExportUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.enums.CameraWarnRuleEnum;
import com.ygkj.water.project.service.CameraOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;

/**
 * @author xq
 * @Description
 * @Date 2021/4/13
 */
@RestController
@RequestMapping("cameraOps")
public class CameraOpsController implements CameraOpsControllerApi {

    @Autowired
    CameraOpsService cameraOpsService;

    @GetMapping("regionAsTree")
    @Override
    public CommonResult regionAsTree() {
        return CommonResult.success(cameraOpsService.regionAsTree());
    }

    @GetMapping("pageCamera")
    @Override
    public CommonResult pageCamera(HikCameraQueryReqVo reqVo) {
        return CommonResult.success(cameraOpsService.pageCamera(reqVo));
    }

    @GetMapping("stats4PageCamera")
    @Override
    public CommonResult stats4PageCamera(HikCameraQueryReqVo reqVo) {
        return CommonResult.success(cameraOpsService.stats4PageCamera(reqVo));
    }

    @PostMapping("exportPageCamera")
    @Override
    public void exportPageCamera(HttpServletResponse response, @RequestBody HikCameraQueryReqVo reqVo) {
        reqVo.setPageNum(-1);
        reqVo.setPageSize(-1);
        if (reqVo.getExportStrategy()==null){
            reqVo.setExportStrategy(0);
        }
        List<HikCameraResVo> cameraList = cameraOpsService.pageCamera(reqVo).getList();
        switch (reqVo.getExportStrategy()){
            case 0:{
                Function<Object,Object> statusMapping=o->{
                    Integer status=(Integer) o;
                    if (status!=null&&status==1){
                        return "在线";
                    }else{
                        return "离线";
                    }
                };
                ExportUtils.exportExcel(cameraList, "视频监控",
                        new String[]{"摄像头名称", "行政区划", "所属工程", "工程类型", "视频状态", "持续时长", "平台路径"},
                        new String[]{"cameraName", "adnm", "projectName", "projectType", "status", "statusDuration", "regionCameraChain"}, response,HikCameraResVo.class, Collections.singletonMap("status",statusMapping));

            }break;
            case 1:{
                Function<Object,Object> regionMapping=o->{
                    String regionCameraChain=(String) o;
                    if (StringUtils.isNotBlank(regionCameraChain)&&regionCameraChain.contains("@")){
                        return regionCameraChain.substring(0,regionCameraChain.lastIndexOf("@"));
                    }else{
                        return org.apache.commons.lang3.StringUtils.EMPTY;
                    }
                };
                Function<Object,Object> isWarnNotifyMapping=o->{
                    Integer isWarnNotify=(Integer) o;
                    if (isWarnNotify==null){
                        return org.apache.commons.lang3.StringUtils.EMPTY;
                    }else if (isWarnNotify==1){
                        return "报警";
                    }else{
                        return "不报警";
                    }
                };
                Function<Object,Object> warnNotifyRuleMapping=o->{
                    CameraWarnRuleEnum warnRuleEnum=CameraWarnRuleEnum.candidateByRuleCode((Integer) o);
                    if (warnRuleEnum==null){
                        return org.apache.commons.lang3.StringUtils.EMPTY;
                    }else {
                        return warnRuleEnum.getRuleDesc();
                    }
                };
                Map<String,Function<Object,Object>> mappingMap=new HashMap<>();
                mappingMap.put("regionCameraChain",regionMapping);
                mappingMap.put("isWarnNotify",isWarnNotifyMapping);
                mappingMap.put("warnNotifyRule",warnNotifyRuleMapping);
                ExportUtils.exportExcel(cameraList, "视频报警管理",
                        new String[]{"区域","摄像头名称", "报警方式", "摄像头负责人", "是否报警", "报警规则"},
                        new String[]{"regionCameraChain","cameraName", "notifyType", "opsName", "isWarnNotify", "warnNotifyRule"}, response,HikCameraResVo.class, mappingMap);

            }break;
        }

    }

    @GetMapping("listBindHistory")
    @Override
    public CommonResult listBindHistory(String cameraIndexCode,Integer pageSize,Integer pageNum) {
        return CommonResult.success(cameraOpsService.listBindHistory(cameraIndexCode,pageSize,pageNum));
    }

    @PostMapping("updateCameraInfo")
    @Override
    public CommonResult updateCameraInfo(@RequestBody HikvisionCameraRecord cameraRecord) {
        return CommonResult.success(cameraOpsService.updateCameraInfo(cameraRecord));
    }

    @PostMapping("delCamera")
    @Override
    public CommonResult delCamera(@RequestParam("cameraIndexCode") String cameraIndexCode) {
        return CommonResult.success(cameraOpsService.delCamera(cameraIndexCode));
    }

    @PostMapping("bindProjByRegion")
    @Override
    public CommonResult bindProjByRegion(@RequestParam("projectId")String projectId, @RequestParam("regionIndexCode")String regionIndexCode) {
        return CommonResult.success(cameraOpsService.bindProjByRegion(projectId,regionIndexCode));
    }

    @PostMapping("bindProjByCamera")
    @Override
    public CommonResult bindProjByCamera(@RequestParam("projectId")String projectId, @RequestParam("cameraIndexCode")String cameraIndexCode) {
        return CommonResult.success(cameraOpsService.bindProjByCamera(projectId,cameraIndexCode));
    }

    @PostMapping("bindOpsByCamera")
    @Override
    public CommonResult bindOpsByCamera(@RequestParam("opsId")String opsId, @RequestParam("cameraIndexCode")String cameraIndexCode) {
        return CommonResult.success(cameraOpsService.bindOpsByCamera(opsId,cameraIndexCode));
    }

    @PostMapping("unbindProjCamera")
    @Override
    public CommonResult unbindProjCamera( @RequestParam("cameraIndexCode")String cameraIndexCode) {
        return CommonResult.success(cameraOpsService.unbindProjCamera(cameraIndexCode));
    }

    @PostMapping("unbindOpsCamera")
    @Override
    public CommonResult unbindOpsCamera(@RequestParam("opsId")String opsId, @RequestParam("cameraIndexCode")String cameraIndexCode) {
        return CommonResult.success(cameraOpsService.unbindOpsCamera(opsId,cameraIndexCode));
    }

    @GetMapping("pageOpsInfo")
    @Override
    public CommonResult pageOpsInfo(CameraOpsQueryReqVo reqVo) {
        return CommonResult.success(cameraOpsService.pageOpsInfo(reqVo));
    }

    @PostMapping("addOrUpdateOps")
    @Override
    public CommonResult addOrUpdateOps(@RequestBody AttCameraOps record) {
        return CommonResult.success(cameraOpsService.addOrUpdateOps(record));
    }

    @PostMapping("delOps")
    @Override
    public CommonResult delOps(@RequestParam("opsId") String opsId) {
        return CommonResult.success(cameraOpsService.delOps(opsId));
    }

    @GetMapping("statsCameraByBasin")
    @Override
    public CommonResult statsCameraByBasin() {
        return CommonResult.success(this.cameraOpsService.statsCameraByBasin());
    }

    @GetMapping("statsCameraByProject")
    @Override
    public CommonResult statsCameraByProject() {
        return CommonResult.success(this.cameraOpsService.statsCameraByProject());
    }

    @GetMapping("statsCameraByAdcd")
    @Override
    public CommonResult statsCameraByAdcd() {
        return CommonResult.success(this.cameraOpsService.statsCameraByAdcd());
    }

    @GetMapping("countCameraStatus")
    @Override
    public CommonResult<List<CountCameraStatus>> countCameraStatus() {
        return CommonResult.success(cameraOpsService.countCameraStatus());
    }

    @GetMapping("pageCameraOpsStatus")
    @Override
    public CommonResult pageCameraOpsStatus(HikCameraQueryReqVo reqVo) {
        return CommonResult.success(this.cameraOpsService.pageCameraOpsStatus(reqVo));
    }

    @PostMapping("updateCameraWarnNotifyRule")
    @Override
    public CommonResult updateCameraWarnNotifyRule(String cameraIndexCode, Boolean isWarnNotify, Integer warnNotifyRule) {
        return CommonResult.success(this.cameraOpsService.updateCameraWarnNotifyRule(cameraIndexCode,isWarnNotify,warnNotifyRule));
    }

    @GetMapping("getWarnTaskState")
    @Override
    public CommonResult getWarnTaskState(String cameraIndexCode) {
        return CommonResult.success(this.cameraOpsService.getWarnTaskState(cameraIndexCode));
    }

    @PostMapping("sendCameraStatusToOps")
    @Override
    public CommonResult sendCameraStatusToOps(@RequestParam("cameraIndexCode")String cameraIndexCode) {
        return CommonResult.success(this.cameraOpsService.sendCameraStatusToOps(cameraIndexCode));
    }

    @GetMapping("historySmsLog")
    @Override
    public CommonResult historySmsLog(@RequestParam("cameraIndexCode") String cameraIndexCode) {
        return CommonResult.success(this.cameraOpsService.historySmsLog(cameraIndexCode));
    }

    @GetMapping("selectCamera")
    @Override
    public CommonResult selectCameras(HikCameraQueryReqVo vo) {
        return CommonResult.success(this.cameraOpsService.selectCameras(vo));
    }

    @GetMapping("/cameraNumStats")
    @Override
    public CommonResult cameraNumStats(@RequestParam("type") int type) {
        return CommonResult.success(cameraOpsService.cameraNumStats(type));
    }

    @GetMapping("/cameraStatusByAdcd4Amap")
    @Override
    public CommonResult cameraStatusByAdcd4Amap(Integer sortType) {
        return CommonResult.success(cameraOpsService.cameraStatusByAdcd4Amap(sortType));
    }

    @GetMapping("/listCameraBindObj")
    @Override
    public CommonResult listCameraBindObj(CamBindObjReqVo reqVo) {
        return CommonResult.success(cameraOpsService.listCameraBindObj(reqVo));
    }

    @GetMapping("/listCameraBindObjType")
    @Override
    public CommonResult listCameraBindObjType() {
        return CommonResult.success(cameraOpsService.listAllCameraBindObjType());
    }
}
