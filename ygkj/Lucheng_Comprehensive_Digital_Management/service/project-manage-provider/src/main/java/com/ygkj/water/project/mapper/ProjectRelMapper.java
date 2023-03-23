package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.water.project.vo.MonitoringStationResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectRelMapper {

    /**
     * 工程关联测站
     *
     * @param vo
     * @return
     */
    List<SlProjStResVo> listRelSt(ProRelStVo vo);

    /**
     * 工程关联水质列表
     *
     * @param projectCode
     * @return
     */
    List<SlProjStResVo> listRelQua(String projectCode);

    /**
     * TODO 分页查询工程巡查
     *
     * @param reqVo
     * @return
     */
    @DS("exchange")
    List<ProjectPatrolResVo> listPatrol(ProjectPatrolReqVo reqVo);

    /**
     * 分页查询工程隐患
     *
     * @param reqVo
     * @return
     */
    @DS("exchange")
    List<ProjectDangerResVo> listDanger(ProjectDangerReqVo reqVo);

    /**
     * TODO 分页查询历史重大险情
     *
     * @param reqVo
     * @return
     */
    @DS("exchange")
    List<ProjectGreatDangerResVo> listGreatDanger(ProjectGreatDangerReqVo reqVo);

    /**
     * 分页或者列表查询监测站点
     *
     * @param reqVo
     * @return
     */
    List<MonitoringStationResVo> listMs(MonitoringStationReqVo reqVo);

    /**
     * 值班负责人
     *
     * @param projectCode
     * @param date
     * @return
     */
    @DS("exchange")
    DutyResVo selectLeader(@Param("projectCode") String projectCode, @Param("date") String date);

    /**
     * 值班人员
     *
     * @param projectCode
     * @param date
     * @return
     */
    @DS("exchange")
    List<DutyResVo> listWorker(@Param("projectCode") String projectCode, @Param("date") String date);

    /**
     * 本地工程编码转换成外部工程编码
     *
     * @param projectCodeMapping
     * @return
     */
    @DS("exchange")
    String selectForeignCode(ProjectCodeMapping projectCodeMapping);

    /**
     * 外部工程编码
     *
     * @param list
     * @return
     */
    @DS("exchange")
    List<ProjectCodeMapping> listMapping(List<String> list);

    /**
     * 今年的汛前检查
     *
     * @param list
     * @return
     */
    @DS("exchange")
    List<BhLgMpFc> listBhFc(List<String> list);

    /**
     * 去年的年度检查
     *
     * @param list
     * @return
     */
    @DS("exchange")
    List<BhLgMpAc> listBhAc(List<String> list);

    /**
     * 最新巡查数据
     *
     * @param list
     * @return
     */
    @DS("exchange")
    List<BhSlPatrolGis> listPatrolWarn(List<String> list);

    /**
     * 未完成的隐患
     *
     * @param list
     * @return
     */
    @DS("exchange")
    List<BhLgPrjHideDanger> listDangerWarn(List<String> list);

    /**
     * 摄像头绑定工程
     *
     * @param reqVo
     */
    void updateProjVideoRel(BatchSlProjStReqVo reqVo);

    /**
     * 编辑测站工程绑定关系
     *
     * @param list
     */
    void batchInsertProjStRel(List<SlProjStR> list);

    /**
     * 去除该工程所有的主站
     *
     * @param projectCode
     */
    void cleanMainSt(String projectCode);

    /**
     * 海塘绑定主站
     *
     * @param slProjStR
     */
    void updateSeawallMainSt(SlProjStR slProjStR);

    /**
     * 清除工程点位绑定关系
     *
     * @param slProjStR
     */
    void delProjStRel(SlProjStR slProjStR);

    /**
     * 工程表主站
     *
     * @param slProjStR
     */
    void delSeawallMainSt(SlProjStR slProjStR);

    /**
     * 工程(未)关联视频
     *
     * @param reqVo
     * @return
     */
    List<SlProjStRResVo> listProjVideoRel(SlProjStReqVo reqVo);

    /**
     * 工程(未)关联测站
     *
     * @param reqVo
     * @return
     */
    List<SlProjStRResVo> listProjStRel(SlProjStReqVo reqVo);

    /**
     * 解除旧的工程视频绑定关系
     *
     * @param slProjStR
     */
    void cleanProjVideoRel(SlProjStR slProjStR);

    /**
     * 设置(取消)主站
     *
     * @param slProjStR
     */
    void updateMainSt(SlProjStR slProjStR);

    /**
     * 绑定各站点数量
     *
     * @param projectCode
     * @return
     */
    CountRelResVo countRelSt(String projectCode);

    /**
     * 绑定摄像头数量
     *
     * @param projectCode
     * @return
     */
    int countRelVideo(String projectCode);

    /**
     * 字典列表
     *
     * @param bhDict
     * @return
     */
    @DS("exchange")
    List<BhDict> listBhDict(BhDict bhDict);

    /**
     * 水库列表
     *
     * @param reqVo
     * @return
     */
    List<WaterLevelWarnResVo> listRes(WaterLevelWarnReqVo reqVo);

    /**
     * 水库水位
     *
     * @param stcdList
     * @param table
     * @return
     */
    @DS("business")
    List<StRsvrR> listRz(@Param("stcdList") List<String> stcdList, @Param("table") String table);

    /**
     * 水闸列表
     *
     * @param reqVo
     * @return
     */
    List<WaterLevelWarnResVo> listWaga(WaterLevelWarnReqVo reqVo);

    /**
     * 水闸水位
     *
     * @param stcdList
     * @param table
     * @return
     */
    @DS("business")
    List<StWasR> listUpz(@Param("stcdList") List<String> stcdList, @Param("table") String table);

    /**
     * 堤防列表
     *
     * @param reqVo
     * @return
     */
    List<WaterLevelWarnResVo> listDike(WaterLevelWarnReqVo reqVo);

    /**
     * 所有工程的
     *
     * @param reqVo
     * @return
     */
    List<RuntimeWarnAllProjectResVo> listRuntimeProject(RuntimeWarnReqVo reqVo);

    /**
     * 汛前检查
     *
     * @param reqVo
     * @return
     */
    @DS("exchange")
    List<BeforeFloodResVo> listBeforeFlood(CheckReqVo reqVo);

    /**
     * 年度检查列表
     *
     * @param reqVo
     * @return
     */
    @DS("exchange")
    List<BhLgMpAcResVo> listYearReport(CheckReqVo reqVo);

    /**
     * 白蚁检查列表
     *
     * @param reqVo
     * @return
     */
    @DS("exchange")
    List<BhLgTermiteCheck> listTermiteCheck(CheckReqVo reqVo);

    /**
     * 返回上传了汛前检查文件的工程
     * @param codeList
     * @param year
     * @return
     */
    @DS("exchange")
    List<JSONObject> listYearBhFc(@Param("list") List<String> codeList, @Param("year") int year);

    /**
     * 返回上传了年度检查文件的工程
     *
     * @param codeList
     * @param year
     */
    @DS("exchange")
    List<JSONObject> listYearCheck(@Param("list") List<String> codeList, @Param("year") int year);

    /**
     * 返回上传了讯后检查文件的工程
     *
     * @param codeList
     * @param year
     */
    List<JSONObject> listAfterCheck(@Param("list") List<String> codeList, @Param("year") int year);

    /**
     * 返回日常巡查的工程
     * @param codeList
     * @param date
     */
    @DS("exchange")
    List<JSONObject> listDailyCheck(@Param("list")List<String> codeList, @Param("date")String date);

    List<JSONObject> getProjList(@Param("list") List<String> codeList);
}
