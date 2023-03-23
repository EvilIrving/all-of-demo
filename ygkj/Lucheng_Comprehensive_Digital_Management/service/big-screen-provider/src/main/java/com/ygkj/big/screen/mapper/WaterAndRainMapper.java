package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.StAstrotdDay;
import com.ygkj.big.screen.model.StPptnR;
import com.ygkj.big.screen.model.StRsvrR;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/28
 */
public interface WaterAndRainMapper {
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
     * 查询水库某时刻左右的水位，如果没查到当前时刻的水位，就查离那个时刻最近的水位
     *
     * @param tables
     * @param stcds
     * @param date
     * @return
     */
    @DS("business")
    List<StRsvrR> selectRecentRsvrWaterLevelByDate(@Param("tables") List<String> tables, @Param("stcds") List<String> stcds, Date date);

    @DS("business")
    @Select("select * from st_astrotd_day where stnm=#{stnm} and `date` between #{start} and #{end}")
    List<StAstrotdDay> selectAstrotdDayByDate(String stnm, Date start, Date end);

    @DS("business")
    List<StPptnR> selectSumRainfall(@Param("tables") List<String> tables,
                                    @Param("stcds") List<String> stcds,
                                    @Param("start") Date start,
                                    @Param("end") Date end);

    @DS("business")
    List<StPptnR> selectPastHoursRainfall(@Param("table") String table, @Param("endTime") String endTime, @Param("hour") Integer hour, @Param("codes") List<String> codes);

    /**
     * 查找最新的数据
     *
     * @param resCode
     * @return
     */
    @DS("business")
    Double findLastRsvrFloodData(@Param("resCode") String resCode);
}
