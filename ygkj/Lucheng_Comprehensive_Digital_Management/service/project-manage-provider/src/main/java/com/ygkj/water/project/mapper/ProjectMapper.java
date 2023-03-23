package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.aspects.annotations.EnableSystemColumnAssignment;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.request.RiverProjectQueryVo;
import com.ygkj.project.model.AttAllRealProjectBase;
import com.ygkj.project.model.BmProject;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProjectMapper {

    @EnableSystemColumnAssignment
    List<AttResBase> selectReservoirByCondition(ProjectManageQueryVo queryVo);

    List<AttPustBase> selectPumpByCondition(ProjectManageQueryVo queryVo);

    List<AttDikeBase> selectDikeByCondition(ProjectManageQueryVo queryVo);

    List<AttMnpdBase> selectMountainPoolByCondition(ProjectManageQueryVo queryVo);

    List<AttWagaBase> selectWagaByCondition(ProjectManageQueryVo queryVo);

    List<AttSeawallBase> selectSeawallByCondition(ProjectManageQueryVo queryVo);

    @Select("SELECT a.seawall_code,a.seawall_name,b.adnm,a.seawall_length AS area_code FROM att_seawall_base a \n" +
            "LEFT JOIN att_adcd_base b ON a.area_code = b.adcd\n" +
            "WHERE a.del_flag = 0")
    List<AttSeawallBase> selectSeawall();

    @Update("UPDATE att_seawall_base SET seawall_length = #{seawallLength}" +
//            "longitude = #{longitude},latitude = #{latitude},seawall_level = #{seawallLevel}," +
//            "safety_time = #{safetyTime},safety_conclusion = #{safetyConclusion},reinforce = #{reinforce}" +
            " WHERE seawall_code = #{seawallCode}")
    int updateSeawall(AttSeawallBase attSeawallBase);

    List<AttHystBase> selectHydristByCondition(ProjectManageQueryVo queryVo);

    List<WaterSupplyProject> selectWaterSupplyProjectByCondition(ProjectManageQueryVo queryVo);

    /**
     * 查询闸站工程
     *
     * @param queryVo
     * @return
     */
    List<AttGateBase> selectGateProjectByCondition(ProjectManageQueryVo queryVo);

    /**
     * 查询农村供水工程
     *
     * @param queryVo
     * @return
     */
    List<AttCwsBase> selectCwsProjectByCondition(ProjectManageQueryVo queryVo);

    /**
     * 查询灌区工程
     *
     * @param queryVo
     * @return
     */
    List<AttIrrBase> selectIrrProjectByCondition(ProjectManageQueryVo queryVo);

    List<BuildingProject> selectBuildingProjectByCondition(ProjectManageQueryVo queryVo);

    List<AttProjBase> selectRiverProject(RiverProjectQueryVo ProjectManageQueryVo);

    List<String> selectProjectType();

    @Select("select * from att_real_all_project_base where del_flag=0 AND longitude IS NOT NULL AND latitude IS NOT NULL ")
    List<ProjectResVo> selectAllProject();

    /**
     * 根据项目编码 查询 工程信息记录
     *
     * @param projCode 项目编码
     * @return
     */
    ProjectResVo selectProjectByProCode(@Param("code") String projCode);

    List<ChartResVo<String, Long>> countProjTypeNumByAdcdBasin(@Param("adcd") String adcd, @Param("basin") String basin, @Param("stType") String stType);

    @Select("SELECT\n" +
            "\tSUM( dike_len )/ 1000 AS dike_length,\n" +
            "\tdike_type \n" +
            "FROM\n" +
            "\tatt_dike_base \n" +
            "WHERE\n" +
            "\tdel_flag = 0 \n" +
            "GROUP BY\n" +
            "\tdike_type UNION ALL\n" +
            "SELECT\n" +
            "\tSUM( seawall_length )/ 1000 AS dike_length,\n" +
            "\t'海塘堤防' AS dike_type \n" +
            "FROM\n" +
            "\tatt_seawall_base \n" +
            "WHERE\n" +
            "\tdel_flag = 0")
    List<Map<String, String>> dikeAndSeawallLengthCount();

    /**
     * 按分类条件查询 统计测站分析
     *
     * @param stType
     * @return
     */
    List<StCountResVo> attStBaseCountByCondition(@Param("stType") String stType, @Param("isWrp") Boolean isWrp);


    /**
     * 按照 行政区划编码 统计测站分析
     *
     * @param stType
     * @return
     */
    List<StCountResVo> attStBaseCountByAreaCode(@Param("stType") String stType, @Param("isWrp") Boolean isWrp);

    /**
     * 按照监测站类型 统计测站分析
     *
     * @param stType
     * @return
     */
    List<StCountResVo> attStBaseCountByStType(@Param("stType") String stType);

    /**
     * 获取工程编码列表
     *
     * @return
     */
    List<String> selectProjCode(@Param("stType") String stType);

    /**
     * 统计测站分析 根据工程类别分析
     *
     * @param projCodes
     * @return
     */
    List<StCountResVo> attStBaseCountByProjType(@Param("projCodes") List<String> projCodes);

    @Select("select count(1) AS `value`,bas AS `KEY` from att_real_all_project_base where del_flag=0\n" +
            "AND FIND_IN_SET(bas,'飞云江,瓯江,鳌江')\n" +
            "GROUP BY `KEY`")
    List<ChartResVo<String, String>> countProjectByProjectType();


    List<ChartResVo<String, Long>> countProjectTypeByBasin(@Param("stType") String stType);


    List<ChartResVo<String, Long>> countProjectTypeByAdcd(@Param("stType") String stType);

    /**
     * 根据codes查出符合条件的工程项目
     *
     * @param codes
     * @return
     */
    List<ProjectResVo> candidateRecords(List<String> codes);

    /**
     * 查询病险工程
     *
     * @param reqVo
     * @return
     */
    List<DilapProjResVo> selectDilapProjByCondition(DilapProjReqVo reqVo);


    /**
     * 查询病险工程
     *
     * @param reqVo
     * @return
     */
    @DS("business")
    List<ProjRiskResVo> selectProjRiskByCondition(ProjRiskReqVo reqVo);

    @Select("SELECT COUNT(1) AS num,project_type AS type FROM att_real_all_project_base \n" +
            "WHERE project_type <> '其他' AND project_type <> '河道'\n" +
            "GROUP BY project_type \n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),\n" +
            "CASE st_type\n" +
            "WHEN 'RR' THEN '水库水位'\n" +
            "WHEN 'ZZ' THEN '河道水位'\n" +
            "WHEN 'TT' THEN '潮位'\n" +
            "END\n" +
            " AS type FROM att_st_base WHERE del_flag = 0 AND FIND_IN_SET(st_type,'RR,ZZ,TT')GROUP BY st_type\n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),'雨情' AS type FROM att_st_base WHERE del_flag = 0 AND is_rain_station = 1\n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),'水闸工情' AS type FROM att_st_base WHERE del_flag = 0 AND wrp_waga = 1 AND st_type='DD'\n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),'堰闸水位' AS type FROM att_st_base WHERE del_flag = 0  AND st_type='DD'\n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),'水质监测' AS type FROM att_bswq_base WHERE del_flag = 0\n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),'积水' AS type FROM att_flood_point WHERE del_flag = 0\n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),'视频' AS type FROM hikvision_camera_record WHERE del_flag = 0\n" +
            "UNION ALL\n" +
            "SELECT COUNT(1),'闸站' AS type FROM att_gate_base WHERE del_flag = 0\n"+
            "UNION ALL\n" +
            "SELECT COUNT(1),\n" +
            "CASE PRO_TYPE\n"+
            "WHEN '码头' THEN '码头'\n" +
            "WHEN '桥梁' THEN '桥梁'\n" +
            "WHEN '道路' THEN '海塘道路交叉出入口'\n" +
            "WHEN '旱闸' THEN '旱闸'\n" +
            "WHEN '箱涵' THEN '箱涵'\n" +
            "WHEN '管理房' THEN '管理房'\n" +
            "WHEN '测站平台' THEN '测站平台'\n" +
            "WHEN '规模以下水闸' THEN '规模以下水闸'\n" +
            "END\n" +
            "AS type FROM bm_acc_build WHERE del_flag = 0 GROUP BY PRO_TYPE\n"+
            "UNION ALL\n" +
            "SELECT COUNT(1),'桩号' AS type FROM bm_seawall_structinfo WHERE del_flag = 0\n"+
            "UNION ALL\n" +
            "SELECT COUNT(1),'防汛仓库' AS type FROM att_warehouse_base WHERE del_flag = 0\n"+
            "UNION ALL\n" +
            "SELECT COUNT(1),'救援队伍' AS type FROM att_team_base WHERE del_flag = 0\n"+
            "UNION ALL\n" +
            "SELECT COUNT(1),'避灾点' AS type FROM avoidance_point WHERE del_flag = 0\n"+
            "UNION ALL\n" +
            "SELECT COUNT(1),'断面' AS type FROM section_configuration \n"
    )
    List<Map<String, String>> haha();

    List<ReservoirProjectResp> reservoirProjectList(ReservoirProjectReq vo);

    List<StatisticsReservoirTypeResp> reservoirProjectStatic(ReservoirProjectReq vo);

    List<SeawallProjectResp> seawallProject(SeawallProjectReq vo);

    List<StatisticsSeawallTypeResp> seawallProjectStatic(SeawallProjectReq vo);

    List<DikeProjectResp> dikeProject(DikeProjectReq vo);

    List<StatisticsDikeTypeResp> dikeProjectStatic(DikeProjectReq vo);

    List<WagaProjectResp> wagaProject(WagaProjectReq vo);

    List<StatisticsWagaTypeResp> wagaProjectStatic(WagaProjectReq vo);

    List<IrrProjectResp> irrProject(IrrProjectReq vo);

    List<StatisticsIrrTypeResp> irrProjectStatic(IrrProjectReq vo);

    List<GateProjectResp> gateProject(GateProjectReq vo);

    List<StatisticsGateTypeResp> gateProjectStatic(GateProjectReq vo);

    List<PustProjectResp> pustProject(PustProjectReq vo);

    List<AttMnpdBaseReqVo> mnpdProject(MnpdProjectReq vo);

    List<StatisticsPustTypeResp> pustProjectStatic(PustProjectReq vo);

    List<ProjectAreaStatic> reservoirProjectAreaStatic(@Param("areaCode") String areaCode);

    List<ProjectAreaStatic> seawallProjectAreaStatic(@Param("areaCode") String areaCode);

    List<ProjectAreaStatic> dikeProjectAreaStatic(@Param("areaCode") String areaCode);

    List<ProjectAreaStatic> wagaProjectAreaStatic(@Param("areaCode") String areaCode);

    List<ProjectAreaStatic> irrProjectAreaStatic(@Param("areaCode") String areaCode);

    List<ProjectAreaStatic> gateProjectAreaStatic(@Param("areaCode") String areaCode);

    List<ProjectAreaStatic> pustProjectAreaStatic(@Param("areaCode") String areaCode);

    /**
     * 列出十大工程
     *
     * @param reqVo
     * @return
     */
    List<AttAllRealProjectBase> listTenProject(AllRealProjectReqVo reqVo);

    /**
     * 列出十大工程
     *
     * @param reqVo
     * @return
     */
    List<AttAllRealProjectBase> listAllRealProject(AllRealProjectReqVo reqVo);


    /**
     * 根据是否病险 统计 对应表 全部 /病险 数量
     *
     * @param dilapProj 是否病险
     * @param tableName 表名
     * @return
     */
    Integer countProjectNumByDilapProjAndType(@Param("dilapProj") Boolean dilapProj, @Param("tableName") String tableName);


    /**
     * 统计提防总长度
     *
     * @return
     */
    @Select("SELECT SUM(dike_len) FROM att_dike_base where del_flag = 0")
    Integer countDikeLen();

    /**
     * 统计海塘总长度
     *
     * @return
     */
    @Select("select sum(seawall_length) from att_seawall_base where del_flag = 0")
    Integer countSeawallLen();

    /**
     * 统计泵站装机流量
     *
     * @return
     */
    @Select("select sum(ins_flow) from att_pust_base where del_flag = 0")
    Integer countInsFlow();

    /**
     * 统计水电站总装机容量
     *
     * @return
     */
    @Select("select sum(tot_ins_cap) from att_hyst_base where del_flag = 0")
    Integer countTotInsCap();

    /**
     * 有效灌溉面积
     *
     * @return
     */
    @Select("select sum(eff_irr_area) from att_irr_base where del_flag = 0")
    Integer countEffIrrArea();

    /**
     * 统计提防总数
     *
     * @return
     */
    Integer statisticalProjectDikeNum();

    /**
     * 统计泵站总数
     *
     * @return
     */
    Integer statisticalProjectPustNum();

    /**
     * 统计山塘总数
     *
     * @return
     */
    Integer statisticalProjectMnpdNum();

    /**
     * 按行政区划统计提防的数量
     *
     * @return
     */
    List<JSONObject> statisticalProjectDikeNumByArea();

    /**
     * 按行政区划统计泵站的数量
     *
     * @return
     */
    List<JSONObject> statisticalProjectPustNumByArea();

    /**
     * 按行政区划统计山塘的数量
     *
     * @return
     */
    List<JSONObject> statisticalProjectMnpdNumByArea();

    /**
     * 按主要功能分类统计山塘数量
     *
     * @return
     */
    List<JSONObject> selectMnpdGroupByZygn();

    /**
     * 查找水库水源地
     *
     * @param watSrcName 水源地名称
     * @return
     */
    List<WatSrcResVo> selectRsvrWatSrc(String watSrcName);

    @Select("select distinct safety_conclusion from att_res_base where del_flag=0 and safety_conclusion is not null and safety_conclusion <>''")
    List<String> rsvrSafetyConclusion();

    @Select("SELECT COUNT(case when safety_conclusion = '安澜建设' then 1 end) aljs, " +
            "COUNT(case when safety_conclusion = '一类塘' then 1 end) ylt, " +
            "COUNT(case when safety_conclusion = '二类塘' then 1 end) elt," +
            "COUNT(case when safety_conclusion = '三类塘' then 1 end) slt, " +
            "COUNT(case when safety_conclusion = '待鉴定' then 1 end) djd FROM att_seawall_base WHERE del_flag = 0")
    HashMap<String, Object> seawallAqjd();

    @Select("SELECT COUNT(case when seawall_level = '1' then 1 end) one,\n" +
            "COUNT(case when seawall_level = '2' then 1 end) two,\n" +
            "COUNT(case when seawall_level = '3' then 1 end) three,\n" +
            "COUNT(case when seawall_level = '4' then 1 end) four, \n" +
            "COUNT(case when seawall_level = '5' then 1 end) five FROM att_seawall_base WHERE del_flag = 0")
    HashMap<String, Object> seawallHtdj();

    List<BmAccBuildResVo> selectBmAccBuildByCondition(ProjectManageQueryVo queryVo);

    @Select("select PRCD,NAME from bm_project")
    List<BmProject> selectAll();

    @Select("SELECT adcd,adnm,level FROM att_adcd_base WHERE country_adcd = '330302000000'")
    List<JSONObject> getLcAdcd();

    @Select("SELECT camera_index_code adcd,camera_name adnm, area_code,project_code FROM hikvision_camera_record WHERE area_code LIKE '330302%' AND camera_source = '1'")
    List<JSONObject> getHikvision();

    @Select("select PRCD adcd ,NAME adnm from bm_project where find_in_set(PRCD,#{code})")
    List<JSONObject> selectProjectByCodes(@Param("code") String projCode);

    @Select("SELECT SUM(point_capacity) FROM avoidance_point WHERE del_flag = 0")
    Integer countPerson();
}
