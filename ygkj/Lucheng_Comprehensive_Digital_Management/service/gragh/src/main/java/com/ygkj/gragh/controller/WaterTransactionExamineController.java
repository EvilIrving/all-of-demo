package com.ygkj.gragh.controller;

import com.ygkj.gragh.WaterTransactionExamineControllerApi;
import com.ygkj.gragh.service.CameraRecordService;
import com.ygkj.gragh.service.ProjectService;
import com.ygkj.gragh.vo.request.CameraRangeQueryVo;
import com.ygkj.gragh.vo.request.ProjectQueryVo;
import com.ygkj.gragh.vo.request.ProjectRangeQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waterTransaction")
public class WaterTransactionExamineController implements WaterTransactionExamineControllerApi {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CameraRecordService cameraRecordService;

    @PostMapping("/projects")
    @Override
    public CommonResult projects(ProjectQueryVo queryVo) {
        return projectService.projectList(queryVo);
    }

    @PostMapping("/cameras")
    @Override
    public CommonResult camerasInRange(CameraRangeQueryVo cameraRangeQueryVo) {
        return CommonResult.success(cameraRecordService.camerasInRange(cameraRangeQueryVo));
    }

    @PostMapping("/rangeProjects")
    @Override
    public CommonResult projectsInRange(ProjectRangeQueryVo projectRangeQueryVo) {
        return projectService.projectsInRange(projectRangeQueryVo);
    }
}
