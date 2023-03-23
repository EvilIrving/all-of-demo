package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.vo.request.ProjectReqVo;
import com.ygkj.gragh.vo.response.*;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface WaterPlanMapper {

    /**
     * 工程列表
     *
     * @param projectReqVo
     * @return
     */
    List<ProjectResVo> listProject(ProjectReqVo projectReqVo);

    /**
     * 在建项目详细信息
     *
     * @param id
     * @return
     */
    @Select("SELECT project_code,project_name,con_standards,con_content,con_plan,con_quality,investment " +
            "FROM building_project WHERE id = #{id}")
    BuildProResVo selectBuild(String id);

    /**
     * 规划项目详细信息
     *
     * @param id
     * @return
     */
    @Select("SELECT pro.project_name,ad.city_adnm,country_adnm,pro.programme_investment " +
            "FROM programme_project pro " +
            "LEFT JOIN att_adcd_base ad ON ad.adcd = pro.area_code " +
            "WHERE pro.id = #{id} ")
    ProgrammeResVo selectProgramme(String id);

    /**
     * 计划项目详细信息
     *
     * @param id
     * @return
     */
    @Select("SELECT project_name,project_plan,investment_progress,investment_type " +
            "FROM plan_project WHERE id = #{id}")
    PlanProResVo selectPlan(String id);

    /**
     * 谋划项目详细信息
     *
     * @param id
     * @return
     */
    @Select("SELECT project_name,estimate_investment,solve_problem " +
            "FROM plot_project WHERE id = #{id}")
    PlotProResVo selectPlot(String id);

    /**
     * 前期项目详细信息
     *
     * @param id
     * @return
     */
    @Select("SELECT project_name,con_content,project_plan,deadline,progress_rate " +
            "FROM early_project WHERE id = #{id}")
    EarlyResVo selectEarly(String id);

    /**
     * 查询该工程已投资金额
     *
     * @param projectCode
     * @return
     */
    @DS("business")
    @Select("SELECT SUM(Investment_amount) FROM project_investment WHERE project_code = #{projectCode}")
    BigDecimal selectInvested(String projectCode);
}
