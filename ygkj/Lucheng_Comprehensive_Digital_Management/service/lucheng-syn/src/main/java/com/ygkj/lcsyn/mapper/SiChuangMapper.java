package com.ygkj.lcsyn.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("sichuang")
public interface SiChuangMapper {

    /**
     *
     *
     * @return
     */
    @Select("SELECT STCD,TM,DRP FROM ST_PPTN_R WHERE TM >= dateadd(minute,-20,GETDATE())")
    List<StPptnR> listPp();

    /**
     * 河道水位
     *
     * @return
     */
    @Select("SELECT STCD,TM,Z FROM ST_RIVER_R WHERE TM >= dateadd(minute,-20,GETDATE())")
    List<StRiverR> listRiver();

    /**
     * 水库水位
     * @return
     */
    @Select("SELECT STCD,TM,RZ FROM ST_RSVR_R WHERE TM >= dateadd(minute,-20,GETDATE())")
    List<StRsvrR> listRsvr();

    /**
     * 潮位
     * @return
     */
    @Select("SELECT STCD,TM,TDZ FROM ST_TIDE_R WHERE TM >= dateadd(minute,-20,GETDATE())")
    List<StTideR> listTide();

    /**
     * 统计获取对应 历年 月降雨量
     * @param year
     * @param stcds
     * @return
     */
    List<AttPerennialRainfall> getAttPerennialRainfallList(@Param("year")Integer year,@Param("stcds")List<String> stcds);

    /**
     * 统计获取对应 历年 月降雨量
     * @param year
     * @param stcds
     * @return
     */
    List<AttLastYearRainfall> getAttLastYearRainfallList(@Param("year")Integer year,@Param("stcds")List<String> stcds);

}
