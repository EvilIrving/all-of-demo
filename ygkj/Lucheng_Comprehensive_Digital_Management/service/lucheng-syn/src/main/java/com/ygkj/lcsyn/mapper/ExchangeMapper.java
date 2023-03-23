package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@DS("exchange")
public interface ExchangeMapper {

    /**
     * 删除本月值班人员信息
     *
     * @param date
     */
    void delDuty(Date date);

    /**
     * 保存新的值班信息
     *
     * @param list
     */
    void batchInsertDuty(List<BhDutyRecord> list);

    /**
     * 批量替换巡查总数据
     *
     * @param list
     */
    void batchReplacePatrol(List<BhSlPatrolGis> list);

    /**
     * 批量替换巡查点数据
     *
     * @param list
     */
    void batchReplacePatrolPosition(List<BhSlPatrolGisPosition> list);

    /**
     * 批量替换工程隐患
     *
     * @param list
     */
    void batchReplaceDanger(List<BhLgPrjHideDanger> list);

    /**
     * 批量替换年度检查
     *
     * @param list
     */
    void batchReplaceYearCheck(List<BhLgMpAc> list);

    /**
     * 批量替换汛前汛后检查
     *
     * @param list
     */
    void batchReplaceFloodCheck(List<BhLgMpFc> list);

    @Delete("DELETE FROM ${tableName}")
    void del(String tableName);

    @Insert("${insertSql}")
    void batchInsert(String insertSql);

    /**
     * 批量保存工程隐患
     *
     * @param list
     */
    void batchInsertDanger(List<BhLgPrjHideDanger> list);

    @Delete("DELETE FROM bh_sl_patrol_gis WHERE patrol_begintime >= #{date}")
    void delPatrol(Date date);

    /**
     * 批量保存巡查数据
     *
     * @param list
     */
    void batchInsertPatrol(List<BhSlPatrolGis> list);

    @Delete("DELETE FROM bh_sl_patrol_gis_position WHERE patrol_tm >= #{date}")
    void delPatrolPosition(Date date);

    /**
     * 批量保存巡查点记录
     * @param list
     */
    void batchInsertPatrolPosition(List<BhSlPatrolGisPosition> list);

    /**
     * 巡查记录点数据
     * @param date
     * @return
     */
    @Select("SELECT * FROM bh_sl_patrol_gis_position WHERE PATROL_TM >= #{date}")
    List<BhSlPatrolGisPosition> listPatrolPosition(Date date);


    void delPatrolPosition2(List<Integer> delList);
}
