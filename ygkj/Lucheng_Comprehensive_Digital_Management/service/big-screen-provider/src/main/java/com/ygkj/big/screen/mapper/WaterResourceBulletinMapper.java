package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.*;
import com.ygkj.entity.SingletonMap;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xq
 * @Description 水资源公报 持久层
 * @Date 2021/7/29
 */
@DS("business")
public interface WaterResourceBulletinMapper {

    List<WrbRainfall> selectWrbRainfall(Integer year, String adcd);

    List<WrbReducedWater> selectWrbReducedWater(Integer year);

    List<WrbStoreRsvr> selectWrbStoreRsvr(Integer year, String resCode);

    List<WrbWaterConsume> selectWrbWaterConsume(Integer year, String adcd);

    List<WrbWaterResource> selectWrbWaterResource(Integer year, String adcd);

    List<WrbWaterSupply> selectWrbWaterSupply(Integer year);

    @Select("select max(`year`) from ${table}")
    Integer selectMaxYearByCondition(@Param("table") String table, @Param("adcd") String adcd);


    /**
     * 获取各个行政区划年均降水量
     *
     * @return
     */
    @DS("basic")
    @Select("select area_code as `key`,cast( sum(rainfall) as decimal) as `value` from att_statistic_county_surface_rainfall where del_flag=0 group by area_code")
    List<SingletonMap<String, BigDecimal>> yearAvgRainOfCounty();

    /**
     * 获取各个行政区划年均降水量
     *
     * @return
     */
    @DS("basic")
    @Select("select sum(rainfall) from att_statistic_county_surface_rainfall where del_flag=0 and  area_code=#{areaCode}")
    BigDecimal countyYearAvgRain(String areaCode);

}
