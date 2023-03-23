package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.enums.ProjectTableNameEnum;
import com.ygkj.gragh.mapper.WaterPlanMapper;
import com.ygkj.gragh.service.WaterPlanService;
import com.ygkj.gragh.vo.request.ProjectReqVo;
import com.ygkj.gragh.vo.response.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("waterPlanService")
public class WaterPlanServiceImpl implements WaterPlanService {

    @Resource
    private WaterPlanMapper waterPlanMapper;

    /**
     * 工程列表
     *
     * @param projectReqVo
     * @return
     */
    @Override
    public List<ProjectResVo> listProject(ProjectReqVo projectReqVo) {
        ProjectTableNameEnum projectTableNameEnum = null;
        switch (projectReqVo.getProjectType()) {
            case 1:
                projectTableNameEnum = ProjectTableNameEnum.BUILDING;
                break;
            case 2:
                projectTableNameEnum = ProjectTableNameEnum.PROGRAMME;
                break;
            case 3:
                projectTableNameEnum = ProjectTableNameEnum.PLAN;
                break;
            case 4:
                projectTableNameEnum = ProjectTableNameEnum.PLOT;
                break;
            case 5:
                projectTableNameEnum = ProjectTableNameEnum.EARLY;
                break;
        }
        projectReqVo.setTableName(projectTableNameEnum.getTableName());
        return waterPlanMapper.listProject(projectReqVo);
    }

    /**
     * 在建工程详细信息
     *
     * @param id
     * @return
     */
    @Override
    public BuildProResVo selectBuild(String id) {
        BuildProResVo buildProResVo = waterPlanMapper.selectBuild(id);
        //查询该工程目前已投资金额
        BigDecimal invested = waterPlanMapper.selectInvested(buildProResVo.getProjectCode());
        buildProResVo.setInvested(invested);
        BigDecimal total = buildProResVo.getInvestment();
        if (invested != null && total != null) {
            buildProResVo.setInvestmentRate(invested.divide(total, 3, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        return buildProResVo;
    }

    /**
     * 规划项目详细信息
     *
     * @param id
     * @return
     */
    @Override
    public ProgrammeResVo selectProgramme(String id) {
        return waterPlanMapper.selectProgramme(id);
    }

    /**
     * 计划项目详细信息
     *
     * @param id
     * @return
     */
    @Override
    public PlanProResVo selectPlan(String id) {
        return waterPlanMapper.selectPlan(id);
    }

    /**
     * 谋划项目详细信息
     *
     * @param id
     * @return
     */
    @Override
    public PlotProResVo selectPlot(String id) {
        return waterPlanMapper.selectPlot(id);
    }

    /**
     * 早期项目详细信息
     *
     * @param id
     * @return
     */
    @Override
    public EarlyResVo selectEarly(String id) {
        return waterPlanMapper.selectEarly(id);
    }
}
