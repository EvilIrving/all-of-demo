package com.ygkj.river.controller;

import com.ygkj.river.RiverPatrolTaskControllerApi;
import com.ygkj.river.model.RiverPatrolTaskEvent;
import com.ygkj.river.service.RiverPatrolTaskService;
import com.ygkj.river.vo.request.*;
import com.ygkj.utils.ExportUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
@RestController
@RequestMapping("riverPatrolTask")
public class RiverPatrolTaskController implements RiverPatrolTaskControllerApi {

    @Autowired
    RiverPatrolTaskService riverPatrolTaskService;

    @GetMapping("camerasOfRiver")
    @Override
    public CommonResult camerasOfRiver(@RequestParam("riverCode") String riverCode) {
        return CommonResult.success(riverPatrolTaskService.cameraOfRiver(riverCode));
    }

    @GetMapping("cameraByCode")
    @Override
    public CommonResult cameraByCode(@RequestParam("cameraCode") String cameraCode) {
        return CommonResult.success(riverPatrolTaskService.cameraByCode(cameraCode));
    }

    /**
     * -------------------------任务巡河-任务---------------------------------
     */
    @GetMapping("taskPage")
    @Override
    public CommonResult taskPage(RptTaskQueryReqVo reqVo) {
        return CommonResult.success(riverPatrolTaskService.taskPage(reqVo));
    }

    @PostMapping("addOrUpdateTask")
    @Override
    public CommonResult addOrUpdateTask(@RequestBody RptTaskEditReqVo reqVo) {
        if (reqVo == null) {
            return CommonResult.failed("0");
        }
        if (StringUtils.isBlank(reqVo.getId())) {
            return CommonResult.success(riverPatrolTaskService.addTask(reqVo));
        } else {
            return CommonResult.success(riverPatrolTaskService.updateTask(reqVo));
        }
    }

    @PostMapping("delTask")
    @Override
    public CommonResult delTask(@RequestParam("id") String id) {
        return CommonResult.success(riverPatrolTaskService.delTask(id));
    }

    @GetMapping("getOneTask")
    @Override
    public CommonResult getOneTask(@RequestParam("id") String id) {
        return CommonResult.success(riverPatrolTaskService.getOneTask(id));
    }

    /**
     * ------------------------任务巡河-事件-----------------------------------
     */
    @GetMapping("eventPage")
    @Override
    public CommonResult eventPage(RptEventQueryReqVo reqVo) {
        return CommonResult.success(riverPatrolTaskService.eventPage(reqVo));
    }

    @PostMapping("addOrUpdateEvent")
    @Override
    public CommonResult addOrUpdateEvent(@RequestBody RptEventEditReqVo reqVo) {
        if (reqVo == null) {
            return CommonResult.failed("0");
        }
        if (StringUtils.isBlank(reqVo.getId())) {
            return CommonResult.success(riverPatrolTaskService.addEvent(reqVo));
        } else {
            return CommonResult.success(riverPatrolTaskService.updateEvent(reqVo));
        }
    }

    @PostMapping("delEvent")
    @Override
    public CommonResult delEvent(@RequestParam("id") String id) {
        return CommonResult.success(riverPatrolTaskService.delEvent(id));
    }

    @GetMapping("getOneEvent")
    @Override
    public CommonResult getOneEvent(@RequestParam("id") String id) {
        return CommonResult.success(riverPatrolTaskService.getOneEvent(id));
    }

    @Override
    @PostMapping("/exportTaskEvent")
    public void exportTaskEvent(RptEventQueryReqVo vo, HttpServletResponse response) {

        List<RiverPatrolTaskEvent> list = riverPatrolTaskService.getAllEvent(vo);

        ExportUtils.exportExcel(list,
                "任务巡河列表" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()),
                new String[]{"任务名称", "巡河时间", "巡河频次", "巡河方式", "河流名称", "行政区划", "所属流域"},
                new String[]{"taskName", "patrolTime", "patrolFreeq", "patrolMode", "riverName", "adnm", "riverBas"},
                response, RiverPatrolTaskEvent.class);
    }

    /**
     * ------------------------任务巡河-问题-----------------------------------
     */
    @PostMapping("addOrUpdateProblem")
    @Override
    public CommonResult addOrUpdateProblem(@RequestBody RptProbEditReqVo reqVo) {
        if (reqVo == null) {
            return CommonResult.failed("0");
        }
        if (StringUtils.isBlank(reqVo.getId())) {
            return CommonResult.success(riverPatrolTaskService.addProblem(reqVo));
        } else {
            return CommonResult.success(riverPatrolTaskService.updateProblem(reqVo));
        }
    }

    @PostMapping("delProblem")
    @Override
    public CommonResult delProblem(@RequestParam("id") String id) {
        return CommonResult.success(riverPatrolTaskService.delProblem(id));
    }

    @GetMapping("getOneProblemByCamera")
    @Override
    public CommonResult getOneProblemByCamera(@RequestParam("eventId") String eventId, @RequestParam("cameraCode") String cameraCode) {
        return CommonResult.success(riverPatrolTaskService.problemByEventIdCameraCode(eventId, cameraCode));
    }

    @GetMapping("getOneProblemById")
    @Override
    public CommonResult getOneProblemById(@RequestParam("probId") String probId) {
        return CommonResult.success(riverPatrolTaskService.problemById(probId));
    }

}
