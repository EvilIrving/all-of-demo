package com.ygkj.client;

import com.ygkj.auth.VO.request.*;
import com.ygkj.auth.api.monitor.model.BusProject;
import com.ygkj.auth.api.monitor.model.BusProjectMatter;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author: fml
 * @Date: 2020/9/8 17:33
 * @Description:
 */

@FeignClient(name = "sca-water-wms")
public interface WmsClient {

    @PostMapping("/waterbase/insert")
    Object insert(@RequestBody @Valid WaterBaseAddReqVo vo);

    @DeleteMapping("/waterbase/delete/{id}")
    Object delete(@PathVariable("id") String id);

    @PutMapping("/waterbase/update")
    Object update(@RequestBody @Valid WaterBaseUpdateReqVo vo);

    @GetMapping("/waterbase/load/{id}")
    Object load(@PathVariable("id") String id);

    @PostMapping("/waterbase/pageListByInfo")
    Object pageListByInfo(WaterBaseByInfoPageReqVo vo);

    @GetMapping("/waterbase/queryAreaNames")
    Object queryAreaNames();

    @PostMapping(value = "/waterbase/parseExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object parseExcel(@RequestPart(value = "file",required = false) MultipartFile file);

    @PostMapping("/waterbase/queryNearbyWaterBaseInfo")
    Object queryNearbyWaterBaseInfo(@RequestParam("lng") Double lng, @RequestParam("lat") Double lat);

    // ---------------------------------------水域变化---------------------------------------------

    //根据行政区划和水域类型查询水域变化信息
    @PostMapping("/waterChangeManage/queryWaterChangeInfo")
    Object queryWaterChangeInfo(@RequestBody @Valid WaterChangePageReqVo vo);

    //根据id查询水域详情记录
    @GetMapping("/waterChangeManage/queryChangeDetail/{id}")
    Object queryChangeDetail(@PathVariable("id") String id);

    //更新水域详情记录
    @PutMapping("/waterChangeManage/updateChangeDetail")
    Object updateWaterChangeInfo(@RequestBody @Valid ChangeDetailUpdateReqVo vo);

    //根据水域变化id获取水域变化详情记录
    @GetMapping("/waterChangeManage/getChangeDetails/{id}")
    Object getChangeDetails(@PathVariable("id") String id);

    //新增水域详情记录（1.查询变化历史表中可添加的年份;2.判断表中是否存在该年份的记录）
    @PostMapping("/waterChangeManage/addChangeDetail")
    Object addChangeDetail(@RequestBody @Valid WaterChangeDetailAddReqVo vo);

    //删除水域详情记录
    @DeleteMapping("/waterChangeManage/deleteChangeDetail/{id}")
    Object deleteChangeDetail(@PathVariable("id") String id);

    @GetMapping("/waterChangeManage//queryWaterDetail/{id}")
    Object queryWaterDetail(@PathVariable("id") String id);

    @PostMapping("/waterChangeManage/queryYearWaterDetailInfo")
    Object queryYearWaterDetailInfo(@RequestBody @Valid WaterChangeYearReqVo vo);

    // -----------------------------------------审批-项目立项-------------------------------------------------

    @PostMapping("/BusProject/insert")
    Object insertBusProject(@RequestBody @Valid BusProject busProject);

    @DeleteMapping("/BusProject/delete/{id}")
    Object deleteBusProject(@PathVariable("id") String id);

    @PutMapping("/BusProject/update")
    Object updateBusProject(@RequestBody @Valid BusProject busProject);

    @GetMapping("/BusProject/load/{id}")
    Object loadBusProject(@PathVariable("id") String id);

    @GetMapping("/BusProject/loadFiles/{id}")
    Object loadProjectFiles(@PathVariable("id") String id);

    @PostMapping("/BusProject/pageList")
    Object pageListBusProject(@RequestBody @Valid BusProjectPageReqVo vo);

    @PostMapping("/BusProject/queryDoneProject")
    Object queryDoneProject(@RequestBody @Valid BusProjectPageReqVo vo);

    @PostMapping("/BusProject/querycCorrectProject")
    Object querycCorrectProject(@RequestBody @Valid BusProjectPageReqVo vo);

    @PostMapping("/BusProject/querycBreakProject")
    Object querycBreakProject(@RequestBody @Valid BusProjectPageReqVo vo);

    @PostMapping("/BusProject/querycDeclaredProject")
    Object querycDeclaredProject(@RequestBody @Valid BusProjectPageReqVo vo);

    @PutMapping("/BusProject/updateReportStatus/{id}")
    Object updateReportStatus(@PathVariable("id") String id);

    @PostMapping("/BusProject/uploadFiles/{id}")
    Object uploadFiles(@PathVariable("id") String id, @RequestBody @Valid ProjectFileReqVos vos);

    @PostMapping("/BusProject/updateReport")
    Object updateReport(@RequestBody @Valid BusProject busProject);

    @PostMapping("/BusProject/addTags")
    Object addTags(@RequestParam("id") String id, @RequestParam("tagId") String tagId);

    @PutMapping("/BusProject/updateTags")
    Object updateTags(@RequestParam("ids") String ids, @RequestParam("tagId") String tagId);

    @DeleteMapping("/BusProject/deletedTags")
    Object deletedTags(@RequestParam("id") String id);

    @PostMapping("/BusProject/addProjectTag")
    Object addProjectTag(@RequestParam("tag") String tag);

    @PostMapping("/BusProject/getProjectTags")
    Object getProgectTags(@RequestParam("stage") String stage);

    @GetMapping("/BusProject/getProjectTagById/{tagId}")
    Object getProjectTagById(@PathVariable("tagId") String tagId);

    @PutMapping("/BusProject/updateProjectTag")
    Object updateProjectTag(@RequestBody @Valid ProjectTagUpdateReqVo vo);

    // ---------------------------------------审批-办理事项----------------------------------------------

    @PostMapping("/projectMatter/insert")
    Object insertMatter(@RequestBody @Valid ProjectMatterAddReqVo vo);

    @DeleteMapping("/projectMatter/delete/{id}")
    Object deleteMatter(@PathVariable("id") String id);

    @DeleteMapping("/projectMatter/deleteMatters")
    Object deleteMatters(@RequestParam("ids") String ids);

    @PutMapping("/projectMatter/update")
    Object updateMatter(@RequestBody @Valid BusProjectMatter busProjectMatter);

    @GetMapping("/projectMatter/load/{id}")
    Object loadMatter(@PathVariable("id") String id);

    @GetMapping("/projectMatter/loadFiles/{id}")
    Object loadMatterFiles(@PathVariable("id") String id);

    @PostMapping("/projectMatter/pageList")
    Object pageListMatter(@RequestBody @Valid ProjectMatterPageReqVo vo);

    @PostMapping("/projectMatter/saveProject")
    Object saveProject(@RequestBody @Valid ProjectDeclareReqVo vo);

    @PostMapping("/projectMatter/submitProject")
    Object submitProject(@RequestBody @Valid ProjectDeclareReqVo vo);

    //------------------------------------------审批-流程处理-------------------------------------------------

    @GetMapping("/projectProcess/getProcessRecords/{id}")
    Object getProcessRecords(@PathVariable("id") String id);

    @PostMapping("/projectProcess/notAccept")
    Object notAccept(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/projectProcess/correction")
    Object correction(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/projectProcess/accept")
    Object accept(@RequestBody @Valid ProjectCheckInfoReqVo vo);

    @PostMapping("/projectProcess/finish")
    Object finish(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/projectProcess/callback")
    Object callback(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/projectProcess/check")
    Object check(@RequestBody @Valid ProjectCheckInfoReqVo vo);

    @PostMapping("/projectProcess/approval")
    Object approval(@RequestBody @Valid ProjectCheckInfoReqVo vo);

    @PostMapping("/projectProcess/saveCheckInfo")
    Object saveCheckInfo(@RequestBody @Valid ProjectCheckInfoReqVo vo);

    @PostMapping("/projectProcess/proofreader")
    Object proofreader(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo);

    @GetMapping("/projectProcess/getProjectLicense/{id}")
    Object getProjectLicense(@PathVariable("id") String id);

    @GetMapping("/projectProcess/getProjectCheckInfo/{id}")
    Object getProjectCheckInfo(@PathVariable("id") String id);

    // ------------------------------------------------ 一张图管理 -----------------------------------------------

    @GetMapping("/pictureManagement/waterBaseTotal")
    Object waterBaseTotal(@RequestParam(value = "waterType", required = false) String waterType);

    @GetMapping("/pictureManagement/waterCountTotal")
    Object waterCountTotal();

    @GetMapping("/pictureManagement/typeAreaRatio")
    Object typeAreaRatio();

    @GetMapping("/pictureManagement/townWaterDetail")
    Object townWaterDetail();

    @GetMapping("/pictureManagement/waterTypeInfo")
    Object waterTypeInfo(
            @RequestParam(value = "waterType", required = false) String waterType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "grade", required = false) String grade);

    @GetMapping("/pictureManagement/waterChangeStatistics")
    Object waterChangeStatistics();

    @GetMapping("/pictureManagement/changeInfoList")
    Object changeInfoList(@RequestParam(value = "waterType", required = false) String waterType,
                          @RequestParam(value = "waterName", required = false) String waterName);

    @GetMapping("/pictureManagement/projectCount")
    Object projectCount();

    @GetMapping("/pictureManagement/queryProjectInfo")
    Object queryProjectInfo(
            @RequestParam(value = "projectType", required = false) String projectType,
            @RequestParam(value = "areaName", required = false) String areaName,
            @RequestParam(value = "keyword", required = false) String keyword);
}
