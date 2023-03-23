package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.project.model.StUpzDwz;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-12-16 19:07
 */
@DS("business")
@Repository
public interface UpdateWasTaskMapper {

    int update(@Param("table") String table, @Param("stWasR") StWasR stWasR);

    int insert(@Param("table") String table, @Param("stWasR") StWasR stWasR);

    @DS("basic")
    @Select("SELECT * FROM st_upz_dwz")
    List<StUpzDwz> selectAll();

    int updateList(@Param("table") String table, @Param("list") List<StWasR> list);

    List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime);

    List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime);

    List<StWasR> selectWasByTimeCode(String table, List<String> stcds, Date dataTime);
}
