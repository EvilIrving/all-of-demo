package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@DS("bh")
public interface BhMapper {

    @Select("SELECT * FROM DUTY_RECORD WHERE DUTY_DATE >= #{date}")
    List<BhDutyRecord> listDuty(Date date);

    @Select("SELECT * FROM SL_PATROL_GIS WHERE PATROL_BEGINTIME >= #{date}")
    List<BhSlPatrolGis> listPatrol(Date date);

    @Select("SELECT * FROM SL_PATROL_GIS_POSITION WHERE PATROL_TM >= #{date}")
    List<BhSlPatrolGisPosition> listPatrolPosition(Date date);

    @Select("SELECT * FROM LG_PRJ_HIDE_DANGER WHERE FOUNDTIME >= '2021-01-01 00:00:00'")
    List<BhLgPrjHideDanger> listDanger();

    @Select("SELECT * FROM LG_MP_AC")
    List<BhLgMpAc> listYearCheck();

    @Select("SELECT * FROM LG_MP_FC")
    List<BhLgMpFc> listFloodCheck();

    @Select("SELECT * FROM ${bhTableName}")
    List<BhDepJobsettinginfo> listAll(String bhTableName);

    @DS("exchange")
    @Select("select id from ${tableName} order by id desc limit 0,1 ")
    String findMaxId(@Param("tableName") String tableName);
}
