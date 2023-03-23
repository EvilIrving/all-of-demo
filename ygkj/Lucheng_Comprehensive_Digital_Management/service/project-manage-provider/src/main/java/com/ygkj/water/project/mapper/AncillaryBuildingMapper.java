package com.ygkj.water.project.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-15 15:56
 */
@Repository
public interface AncillaryBuildingMapper {
    List<Map<String,Object>> selectAncillaryBuilding(@Param("projectCategory") String projectCategory, @Param("prcd") String prcd);
}
