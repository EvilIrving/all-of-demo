package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.*;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-05-06 9:42
 * @description  省厅数据操作mapper
 */
@DS("basic")
public interface ProvinceDataMapper {

    /**
     * 批量插入防御对象
     * @param list
     * @return
     */
    int replaceDefenObject(List<PvDefenObject> list);

    @Select("select max(tong_time) from pv_defen_object ")
    Date findDefenObjectMaxTongDate();

    /**
     * 批量插入防御对象名录表
     * @param list
     * @return
     */
    int replaceDefenObjectBase(List<PvDefenObjectBase> list);

    @Select("select max(tong_time) from pv_defen_object_base")
    Date findDefenObjectBaseMaxTongDate();

    /**
     * 批量插入 山洪预警责任人信息表
     * @param list
     * @return
     */
    int replacePvPchargeInfo(List<PvPchargeInfo> list);

    @Select("select max(tong_time) from pv_pcharge_info ")
    Date findPchargeInfoMaxTongDate();

    /**
     * 批量插入 山洪预警责任人名录表
     * @param list
     * @return
     */
    int replacePvPchargeBase(List<PvPchargeBase> list);

    @Select("select max(tong_time) from pv_pcharge_base")
    Date findPchargeBaseMaxTongDate();

    /**
     * 批量插入  山洪易发区影响人口
     * @param populationList
     * @return
     */
    int replacePvAffectPopulation(List<PvFloodAffectPopulation> populationList);

    @Select("select max(tong_time) from pv_flood_affect_population")
    Date findAffectPopulationMaxTongDate();

    /**
     * 批量插入 防御对象信息
     * @param populationList
     * @return
     */
    int replacePvPrevObjInfo(List<PvPrevObjInfo> populationList);

    @Select("select max(tong_time) from pv_prev_obj_info")
    Date findPrevObjInfoMaxTongDate();

    /**
     * 批量插入 防御对象名录
     * @param objBaseList
     * @return
     */
    int replacePvPrevObjBase(List<PvPrevObjBase> objBaseList);

    @Select("select max(tong_time) from pv_prev_obj_base")
    Date findPrevObjBaseMaxTongDate();

    @Select("select max(tong_time) from pv_flood_warning_target")
    Date findFloodWarningTargetMaxTongDate();

    /**
     * 批量插入 山洪灾害防治区重要村落预警指标成果信息表
     * @param targetList
     * @return
     */
    int replacePvFloodWarningTarget(List<PvFloodWarningTarget> targetList);

    @Select("select max(tong_time) from pv_flood_warning_target_directory")
    Date findFloodWarningTargetDirectoryMaxTongDate();

    /**
     * 批量插入 山洪灾害防治区重要村落预警指标名录表
     * @param directoryList
     * @return
     */
    int replacePvFloodWarningTargetDirectory(List<PvFloodWarningTargetDirectory> directoryList);

    @Select("select max(warn_time) from pv_flood_info")
    Date findFloodInfoMaxWarnDate();

    int replacePvFloodInfo(List<PvFloodInfo> infoList);

    int replaceMountainPondWarn(List<MountainPondWarnResponse> result);

    int replaceSubAdcd(List<SubAdcd> list);
}
