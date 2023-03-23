package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.PrecedeAffectRainfall;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/1
 */
@DS("business")
public interface PrecedeAffectRainfallMapper {


    int insert(PrecedeAffectRainfall rainfall);

    List<PrecedeAffectRainfall> selectNewest();

    List<PrecedeAffectRainfall> selectByAreaCodeTimeInterval(String areaCode, Date start, Date end);

    List<PrecedeAffectRainfall> selectByAreaCodeListTime(List<String> areaCodeList, Date date);

    List<PrecedeAffectRainfall> selectByAreaCodeListTimeInterval(List<String> areaCodeList, Date start, Date end);

    BigDecimal selectAvgDayRainfall(List<String> stcdList, Date date);
}
