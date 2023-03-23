package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/3/1
 */
@DS("business")
public interface ExtremRainfallMapper {
    @Select("select * from extrem_rainfall")
    @ResultType(Map.class)
    List<Map<String, Object>> selectExtremRainfall();
}
