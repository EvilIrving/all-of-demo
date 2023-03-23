package com.ygkj.river.service;

import cn.hutool.json.JSONObject;
import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.vo.request.AttBlueProjectRecVo;
import com.ygkj.river.vo.request.AttBlueProjectReqVo;
import com.ygkj.river.vo.response.AttBlueProjectResVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lxl
 * @create 2021-08-06 9:30
 * @description 蓝线监管项目
 */
public interface AttBlueProjectService {

    /**
     * 新增监管项目
     * @param attBlueProject
     * @return
     */
    Boolean addProject(AttBlueProjectRecVo attBlueProject) throws Exception;

    /**
     *  条件查询工程列表
     * @param reqVo
     * @return
     */
    PageVO<AttBlueProjectResVo> projectList(AttBlueProjectReqVo reqVo);

    /**
     *  更新对应工程信息
     * @param attBlueProject
     * @return
     * @throws Exception
     */
    Boolean updateProject(AttBlueProject attBlueProject) throws Exception;

    /**
     * 获取监管工程详情
     * @param projectId
     * @return
     */
    Object projectDetail(String projectId);

    /**
     * 批后监管按流域分布统计
     * @return
     */
    List<JSONObject> projectStatisByBasin();

    /**
     * 批后监管按河道分布统计
     * @return
     */
    List<JSONObject> projectStatisByRiverLevel();

    /**
     * 批后监管按行政区划统计
     * @return
     */
    List<JSONObject> projectStatisByAdcd();

    /**
     * 批后监管按项目阶段统计
     * @return
     */
    List<JSONObject> projectStatisByProjectStage();


    /**
     * 首页统计接口
     * @return
     */
    Object homeOverView();

    Object homeOverViewByAdcd();

    /**
     * 删除对应项目根据id
     * @param id
     * @return
     */
    Boolean deleteProjectById(String id);

    AttBlueProject findById(String id);

    /**
     * 导出详表
     * @param reqVo
     * @param response
     */
    void exportDetailTable(AttBlueProjectReqVo reqVo, HttpServletResponse response);

    /**
     * 组件-计算监管次数
     * @return
     */
    Object projectInspectionNum(Integer year);

    /**
     * 组件-按行政区划计算问题数量和参与人数
     * @return
     */
    Object projectProblemNum();

    /**
     * 完成率和监察次数
     * @return
     */
    Object inspectionFinishRateAndNumber();

    /**
     * 首页统计
     * @return
     */
    Object projectStaticsForHome();

}
