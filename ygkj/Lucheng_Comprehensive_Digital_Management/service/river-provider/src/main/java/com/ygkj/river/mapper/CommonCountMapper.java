package com.ygkj.river.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommonCountMapper {

    List<Map<String, Integer>> CommonCountByColumn(@Param("table") String table, @Param("column") String column
            , @Param("map") Map<String, String> map);
}
