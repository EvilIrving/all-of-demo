package com.ygkj.controller;

import com.ygkj.auth.VO.request.*;
import com.ygkj.auth.api.monitor.model.BusProject;
import com.ygkj.auth.api.monitor.model.BusProjectMatter;
import com.ygkj.client.WmsClient;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: fml
 * @Date: 2020/9/8 20:52
 * @Description:
 */
//@Api(tags = "审批")
//@RestController
//@RequestMapping("/BusProject")
public class BusProjectController {

    @Autowired
    private WmsClient wmsClient;

    /**
     * 新增
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "新增待办项目信息接口")
    @PostMapping("/insert")
    public Object insert(@RequestBody @Valid BusProject busProject) {
        return wmsClient.insertBusProject(busProject);
    }

    /**
     * 刪除
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "刪除待办项目信息接口")
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable("id") String id) {
        return wmsClient.deleteBusProject(id);
    }

    /**
     * 更新
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "更新待办项目信息接口")
    @PutMapping("/update")
    public Object update(@RequestBody @Valid BusProject busProject) {
        return wmsClient.updateBusProject(busProject);
    }

    /**
     * 查询 根据主键 id 查询
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "根据id查询项目详情接口")
    @GetMapping("/load/{id}")
    public Object load(@PathVariable("id") String id) {
        return wmsClient.loadBusProject(id);
    }

    /**
     * 查询 根据项目id查询项目申请资料
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "根据项目id查询项目申请资料接口")
    @GetMapping("/loadProjectFiles/{id}")
    public Object loadProjectFiles(@PathVariable("id") String id) {
        return wmsClient.loadProjectFiles(id);
    }

    /**
     * 查询 分页查询-待办项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询待办项目信息接口")
    @PostMapping("/pageList")
    public Object pageList(@RequestBody @Valid BusProjectPageReqVo vo) {
        return wmsClient.pageListBusProject(vo);
    }

    /**
     * 查询 分页查询-已办项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询已办项目信息接口")
    @PostMapping("/queryDoneProject")
    public Object queryDoneProject(@RequestBody @Valid BusProjectPageReqVo vo) {
        return wmsClient.queryDoneProject(vo);
    }

    /**
     * 查询 分页查询-补正项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询补正项目信息接口")
    @PostMapping("/querycCorrectProject")
    public Object querycCorrectProject(@RequestBody @Valid BusProjectPageReqVo vo) {
        return wmsClient.querycCorrectProject(vo);
    }

    /**
     * 查询 分页查询-退办项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询退办项目信息接口")
    @PostMapping("/querycBreakProject")
    public Object querycBreakProject(@RequestBody @Valid BusProjectPageReqVo vo) {
        return wmsClient.querycBreakProject(vo);
    }

    /**
     * 查询 分页查询-已申报待办项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询已申报待办项目信息接口")
    @PostMapping("/querycDeclaredProject")
    public Object querycDeclaredProject(@RequestBody @Valid BusProjectPageReqVo vo) {
        return wmsClient.querycDeclaredProject(vo);
    }

    /**
     * 上报
     *
     * @author fml
     * @date 2020/09/18
     **/
    @ApiOperation(value = "根据项目id上报")
    @PutMapping("/updateReportStatus/{id}")
    public Object updateReportStatus(@PathVariable("id") String id) {

        return wmsClient.updateReportStatus(id);
    }

    /**
     * 上传资料
     *
     * @author fml
     * @date 2020/09/18
     **/
    @ApiOperation(value = "根据项目id上传资料")
    @PostMapping("/uploadFiles/{id}")
    public Object uploadFiles(@PathVariable("id") String id, @RequestBody @Valid ProjectFileReqVos vos) {

        return wmsClient.uploadFiles(id, vos);
    }

    /**
     * 未申报项目修改时上报
     *
     * @author fml
     * @date 2020/09/18
     **/
    @ApiOperation(value = "未申报项目修改时上报")
    @PostMapping("/updateReport")
    public Object updateReport(@RequestBody @Valid BusProject busProject) {

        return wmsClient.updateReport(busProject);
    }

    @ApiOperation(value = "根据项目id添加自定义标签")
    @PostMapping("/addTags")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "项目id"),
            @ApiImplicitParam(name = "tagId", value = "自定义标签id")})
    public Object addTags(@RequestParam("id") String id, @RequestParam("tagId") String tagId) {

        return wmsClient.addTags(id, tagId);
    }

    @ApiOperation(value = "根据项目ids(批量)修改自定义标签")
    @PutMapping("/updateTags")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "项目id,多个以逗号分隔"),
            @ApiImplicitParam(name = "tagId", value = "自定义标签")})
    public Object updateTags(@RequestParam("ids") String ids, @RequestParam("tagId") String tagId) {

        return wmsClient.updateTags(ids, tagId);
    }

    @ApiOperation(value = "根据项目id删除自定义标签")
    @DeleteMapping("/deletedTags")
    public Object deletedTags(@RequestParam("id") String id) {

        return wmsClient.deletedTags(id);
    }

    @ApiOperation(value = "新增项目自定义标签")
    @PostMapping("/addProjectTag")
    public Object addProjectTag(@RequestParam("tag") String tag) {

        return wmsClient.addProjectTag(tag);
    }

    @ApiOperation(value = "获取自定义标签（包含标签所存在项目数量）")
    @ApiImplicitParam(name = "stage", value = "项目阶段(1待办，2已办，3补正，4退办)")
    @PostMapping("/getProjectTags")
    public Object getProjectTags(@RequestParam("stage") String stage) {

        return wmsClient.getProgectTags(stage);
    }

    @ApiOperation(value = "根据标签id获取自定义标签信息")
    @ApiImplicitParam(name = "tagId", value = "标签id")
    @GetMapping("/getProjectTagById/{tagId}")
    public Object getProjectTagById(@PathVariable("tagId") String tagId) {

        return wmsClient.getProjectTagById(tagId);
    }

    @ApiOperation(value = "修改自定义标签")
    @PutMapping("/updateProjectTag")
    public Object updateProjectTag(@RequestBody @Valid ProjectTagUpdateReqVo vo) {

        return wmsClient.updateProjectTag(vo);
    }

    // -------------------------------------办理事项---------------------------------------------

    /**
     * 新增办理事项
     *
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "新增办理事项接口")
    @PostMapping("/insertMatter")
    public Object insertMatter(@RequestBody @Valid ProjectMatterAddReqVo vo) {

        return wmsClient.insertMatter(vo);
    }

    /**
     * 刪除办理事项
     *
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "刪除办理事项接口")
    @DeleteMapping("/deleteMatter/{id}")
    public Object deleteMatter(@PathVariable("id") String id) {
        return wmsClient.deleteMatter(id);
    }

    /**
     * 批量删除办理事项
     *
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "批量刪除办理事项(id以 , 拼接)")
    @DeleteMapping("/deleteMatters")
    public Object deleteMatters(@RequestParam("ids") String ids) {
        return wmsClient.deleteMatters(ids);
    }

    /**
     * 修改办理事项
     *
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "修改办理事项接口")
    @PutMapping("/updateMatter")
    public Object updateMatter(@RequestBody @Valid BusProjectMatter busProjectMatter) {
        return wmsClient.updateMatter(busProjectMatter);
    }

    /**
     * 根据id查询办理事项
     *
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "根据id查询办理事项")
    @GetMapping("/loadMatter/{id}")
    public Object loadMatter(@PathVariable("id") String id) {
        return wmsClient.loadMatter(id);
    }

    /**
     * 根据id查询办理事项资料
     *
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "根据事项id查询办理事项资料")
    @GetMapping("/loadMatterFiles/{id}")
    public Object loadMatterFiles(@PathVariable("id") String id) {
        return wmsClient.loadMatterFiles(id);
    }

    /**
     * 分页查询办理事项
     *
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "分页查询办理事项")
    @PostMapping("/pageListMatter")
    public Object pageListMatter(@RequestBody @Valid ProjectMatterPageReqVo vo) {
        return wmsClient.pageListMatter(vo);
    }

    //申报-保存（未上报）
    @ApiOperation(value = "申报-保存（未上报）")
    @PostMapping("/saveProject")
    public Object saveProject(@RequestBody @Valid ProjectDeclareReqVo vo) {

        return wmsClient.saveProject(vo);
    }

    //申报-上报 (保存并在流程记录表中添加记录）
    @ApiOperation(value = "申报-上报")
    @PostMapping("/submitProject")
    public Object submitProject(@RequestBody @Valid ProjectDeclareReqVo vo) {

        return wmsClient.submitProject(vo);
    }

    //----------------------------------------------流程处理-----------------------------------------------------------

    @ApiOperation(value = "根据项目id获取办理流程")
    @GetMapping("/getProcessRecords/{id}")
    public Object getProcessRecords(@PathVariable("id") String id) {

        return wmsClient.getProcessRecords(id);
    }

    // 不予受理，退办项目，审批状态更改为 2，流程记录表记录相关信息
    @ApiOperation(value = "不予受理")
    @PostMapping("/notAccept")
    public Object notAccept(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo) {

        return wmsClient.notAccept(vo);
    }

    // 补正不齐，补正项目，审批状态更改为 3，流程记录表记录相关信息
    @ApiOperation(value = "补正不齐")
    @PostMapping("/correction")
    public Object correction(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo) {

        return wmsClient.correction(vo);
    }

    // 受理，待办项目，审批状态更改为 4，流程记录表记录相关信息
    @ApiOperation(value = "受理")
    @PostMapping("/accept")
    public Object accept(@RequestBody @Valid ProjectCheckInfoReqVo vo) {

        return wmsClient.accept(vo);
    }

    // 办结，已办项目，审批状态更改为 8，流程记录表记录相关信息
    @ApiOperation(value = "办结")
    @PostMapping("/finish")
    public Object finish(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo) {

        return wmsClient.finish(vo);
    }

    // 交办上一人，根据项目id查询审批状态，更改审批状态，更新流程记录
    @ApiOperation(value = "交办上一人")
    @PostMapping("/callback")
    public Object callback(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo) {

        return wmsClient.callback(vo);
    }

    // 审核，保存审核信息，审批状态更改为 5，更新流程记录
    @ApiOperation(value = "审核")
    @PostMapping("/check")
    public Object check(@RequestBody @Valid ProjectCheckInfoReqVo vo) {

        return wmsClient.check(vo);
    }

    // 签批，审批状态更改为 6，更新流程记录
    @ApiOperation(value = "签批")
    @PostMapping("/approval")
    public Object approval(@RequestBody @Valid ProjectCheckInfoReqVo vo) {

        return wmsClient.approval(vo);
    }

    // 保存，保存审核信息（这里要判断审核阶段还是签批阶段）
    @ApiOperation(value = "保存")
    @PostMapping("/saveCheckInfo")
    public Object saveCheckInfo(@RequestBody @Valid ProjectCheckInfoReqVo vo) {

        return wmsClient.saveCheckInfo(vo);
    }

    // 校对，已办项目，审批状态更改为 7，更新流程记录
    @ApiOperation(value = "校对")
    @PostMapping("/proofreader")
    public Object proofreader(@RequestBody @Valid ProjectAssigneeOpinionReqVo vo) {

        return wmsClient.proofreader(vo);
    }

    // 根据项目id获取批复或行政许可信息
    @ApiOperation(value = "根据项目id获取批复或行政许可信息")
    @GetMapping("/getProjectLicense/{id}")
    public Object getProjectLicense(@PathVariable("id") String id) {
        return wmsClient.getProjectLicense(id);
    }

    // 根据项目id和当前状态获取审核状态
    @ApiOperation(value = "根据项目id获取审核信息")
    @GetMapping("/getProjectCheckInfo/{id}")
    public Object getProjectCheckInfo(@PathVariable("id") String id) {

        return wmsClient.getProjectCheckInfo(id);
    }

}
