package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.model.FloodPoint;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("business")
public interface FloodPointMapper {
    @Select("SELECT DISTINCT(name) AS name,id,longitude,latitude FROM flood_point GROUP BY name")
    List<FloodPoint> floodPoints();


    List<FloodPoint> candidateFloodPoints(@Param("codes") List<String> codes);
}
