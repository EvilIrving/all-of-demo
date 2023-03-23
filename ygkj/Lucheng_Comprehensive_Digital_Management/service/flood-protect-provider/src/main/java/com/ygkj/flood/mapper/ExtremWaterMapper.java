package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/3/1
 */
@DS("business")
public interface ExtremWaterMapper {
    List<Map<String, Object>> selectExtremWater(@Param("stName") String stName);
}
