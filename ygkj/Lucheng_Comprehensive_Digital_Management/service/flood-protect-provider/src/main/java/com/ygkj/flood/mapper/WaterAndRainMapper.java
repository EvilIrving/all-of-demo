package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.StBase;
import com.ygkj.flood.model.StRiverR;
import com.ygkj.flood.model.StRsvrR;
import com.ygkj.flood.model.StWasR;
import com.ygkj.flood.model.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/15
 */
public interface WaterAndRainMapper {
    /**
     * 检查数据表格是否存在
     *
     * @param tableName
     * @return
     */
    @DS("business")
    @Select("SHOW TABLES LIKE #{tableName}")
    String checkBusinessTableExist(@Param("tableName") String tableName);

    /**
     * 查询最新的水库水位
     *
     * @param tables
     * @param stcds
     * @return
     */
    @DS("business")
    List<StRsvrR> selectNewestRsvrWaterLevel(@Param("tables") List<String> tables, @Param("stcds") List<String> stcds);

    /**
     * 查询最新的河道水位
     *
     * @param tables
     * @param stcds
     * @return
     */
    @DS("business")
    List<StRiverR> selectNewestRiverLevel(@Param("tables") List<String> tables, @Param("stcds") List<String> stcds);

    /**
     * 查询最新的大中型水库水位
     *
     * @param table
     * @param stcds
     * @return
     */
    @DS("business")
    List<StRsvrR> selectNewestLargeMidReservoirWaterLevel(@Param("table") String table, @Param("stcds") List<String> stcds);

    /**
     * 查询最新的水闸数据
     *
     * @param table
     * @param stcds
     * @return
     */
    @DS("business")
    List<StWasR> selectNewestWagaWaterLevel(@Param("table") String table, @Param("stcds") List<String> stcds);

    @DS("business")
    @Select("SELECT stcd,tm,sum(drp) AS drp from ${table} " +
            "WHERE tm >= #{time} " +
            "GROUP BY stcd ORDER BY drp DESC LIMIT 10")
    List<StPptnR> top10Rainfall(String table, String time);

    @DS("basic")
    @Select("SELECT * FROM att_st_base WHERE del_flag = 0 AND area_code IS NOT NULL")
    List<AttStBase> selectStation();

    @DS("basic")
    List<StBase> findLargeAndMidReservoirStation(String basin);
}
