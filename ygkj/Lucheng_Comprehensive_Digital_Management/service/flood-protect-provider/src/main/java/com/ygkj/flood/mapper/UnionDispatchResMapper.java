package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.FloodPoint;
import com.ygkj.flood.model.StRiverRYyyymm;
import com.ygkj.flood.vo.response.ProResInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnionDispatchResMapper {

    List<ProResInfo> getResList(String projectCode);

    @DS("business")
    List<StRiverRYyyymm> getResLevels(@Param("tableName") String tableName);

    @DS("business")
    List<FloodPoint> listNewestFloodPoint(Integer recentDays);
}
