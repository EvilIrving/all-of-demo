package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.BhDict;
import com.ygkj.project.model.BhLgTermiteCheck;
import com.ygkj.project.model.FloodRiskCenter;
import com.ygkj.project.model.SlProjStR;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.vo.MonitoringStationResVo;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public interface ProjectRelService {

    /**
     * 关联测站
     *
     * @param vo
     * @return
     */
    List<SlProjStResVo> listSt(ProRelStVo vo);

    /**
     * 分页查询巡查列表
     *
     * @param reqVo
     * @return
     */
    PageVO<ProjectPatrolResVo> pagePatrol(ProjectPatrolReqVo reqVo);

    /**
     * 分页查询隐患列表
     *
     * @param reqVo
     * @return
     */
    PageVO<ProjectDangerResVo> pageDanger(ProjectDangerReqVo reqVo);

    /**
     * 分页查询历史重大险情
     *
     * @param reqVo
     * @return
     */
    PageVO<ProjectGreatDangerResVo> pageGreatDanger(ProjectGreatDangerReqVo reqVo);

    /**
     * 分页或者列表查询监测站点
     *
     * @param reqVo
     * @return
     */
    PageVO<MonitoringStationResVo> listMs(MonitoringStationReqVo reqVo);

    /**
     * 值班人员查询
     *
     * @param projectCode
     * @return
     */
    TwoDutyResVo selectDuty(String projectCode);

    /**
     * 管理信息
     *
     * @param projectCode
     * @return
     */
    ManageInfoResVo selectManageInfo(String projectCode);

    /**
     * 运行报警
     *
     * @param projectCode
     * @return
     */
    List<RuntimeWarnResVo> selectRuntimeWarn(String projectCode);

    /**
     * 工程绑定测站
     *
     * @param reqVo
     */
    void updateProjRel(BatchSlProjStReqVo reqVo);

    /**
     * 清除工程绑定关系
     *
     * @param slProjStR
     */
    void delProjRel(SlProjStR slProjStR);

    /**
     * 分页查询工程绑定站点
     *
     * @param reqVo
     */
    PageVO<SlProjStRResVo> pageProjRel(SlProjStReqVo reqVo);

    /**
     * 设置主站
     *
     * @param slProjStR
     */
    void updateMainSt(SlProjStR slProjStR);

    /**
     * 各站点数量
     *
     * @param projectCode
     * @return
     */
    CountRelResVo countRel(String projectCode);

    /**
     * 本地海塘编码转成标化海塘编码
     *
     * @param seawallCode
     * @return
     */
    String transBhSeawall(String seawallCode);

    /**
     * 导出巡查人员
     *
     * @param reqVo
     * @param response
     */
    void exportPagePatrol(ProjectPatrolReqVo reqVo, HttpServletResponse response);

    /**
     * 字典列表
     *
     * @param bhDict
     * @return
     */
    List<BhDict> listDict(BhDict bhDict);

    /**
     * 导出工程隐患
     *
     * @param reqVo
     * @param response
     */
    void exportPageDanger(ProjectDangerReqVo reqVo, HttpServletResponse response);

    /**
     * 工程超警
     *
     * @param reqVo
     * @return
     */
    PageVO<WaterLevelWarnResVo> waterLevelWarn(WaterLevelWarnReqVo reqVo);

    /**
     * 工程运行报警统计
     *
     * @param reqVo
     * @return
     */
    List<RuntimeWarnAllProjectResVo> listRuntimeWarn(RuntimeWarnReqVo reqVo);

    /**
     * 汛前检查列表
     *
     * @param reqVo
     * @return
     */
    PageVO<BeforeFloodResVo> pageBeforeFlood(CheckReqVo reqVo);

    /**
     * 年度检查列表
     *
     * @param reqVo
     * @return
     */
    PageVO<BhLgMpAcResVo> pageYearReport(CheckReqVo reqVo);

    /**
     * 白蚁检查
     *
     * @param reqVo
     * @return
     */
    PageVO<BhLgTermiteCheck> pageTermiteCheck(CheckReqVo reqVo);

    /**
     * 防汛风险C位
     * @return
     */
    FloodRiskCenter floodRiskCenter();

    HashMap<String, Integer> waterLevelWarnCount();

    List<JSONObject> getProjList(List<String> codeList);
}
