package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.HazardPoint;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("business")
public interface HazardPointMapper {

    @Select("SELECT area,area_unit,address,longitude,latitude,capacity_unit,capacity,area_code,street_name,area_name,name,place_code " +
            "FROM hazard_point WHERE del_flag = 0 AND (longitude IS NOT NULL AND latitude IS NOT NULL)")
    List<HazardPoint> hazardPoints();
}
