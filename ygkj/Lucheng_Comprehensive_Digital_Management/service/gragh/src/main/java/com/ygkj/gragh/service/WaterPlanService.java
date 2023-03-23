package com.ygkj.gragh.service;

import com.ygkj.gragh.vo.request.ProjectReqVo;
import com.ygkj.gragh.vo.response.*;

import java.util.List;

public interface WaterPlanService {

    /**
     * 工程列表
     *
     * @param projectReqVo
     * @return
     */
    List<ProjectResVo> listProject(ProjectReqVo projectReqVo);

    /**
     * 在建工程详细信息
     *
     * @param id
     * @return
     */
    BuildProResVo selectBuild(String id);

    /**
     * 规划项目详细信息
     *
     * @param id
     * @return
     */
    ProgrammeResVo selectProgramme(String id);

    /**
     * 计划项目详细信息
     *
     * @param id
     * @return
     */
    PlanProResVo selectPlan(String id);

    /**
     * 谋划项目详细信息
     *
     * @param id
     * @return
     */
    PlotProResVo selectPlot(String id);

    /**
     * 早期项目详细信息
     *
     * @param id
     * @return
     */
    EarlyResVo selectEarly(String id);
}
