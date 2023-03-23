package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttBswqBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StZqrlB;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StationMapper {

    @Select("SELECT * FROM att_st_base WHERE del_flag = 0 AND st_long IS NOT NULL AND st_lat IS NOT NULL " +
            "AND FIND_IN_SET (st_type,'RR,PP,TT,DD,ZZ')")
    List<AttStBase> stations();

    @Select("SELECT * FROM att_bswq_base WHERE del_flag = 0 AND longitude IS NOT NULL AND latitude IS NOT NULL")
    List<AttBswqBase> waterQualityStations();

    @Select("SELECT * FROM att_st_base WHERE del_flag = 0 AND FIND_IN_SET(st_code,#{codes})")
    List<AttStBase> candidateStation(String codes);

    @Select("SELECT * FROM att_bswq_base WHERE del_flag = 0 AND longitude IS NOT NULL AND latitude IS NOT NULL" +
            " AND FIND_IN_SET(`code`,#{codes})")
    List<AttBswqBase> candidateWaterQualityStations(String codes);

    @Select("SELECT * FROM att_st_base WHERE del_flag = 0 AND st_type=#{stType} AND FIND_IN_SET(proj_code,#{projCodes})")
    List<AttStBase> selectStationByProjCode(String projCodes, String stType);

    @Select("SELECT * FROM att_st_base WHERE del_flag = 0 AND st_type=#{stType}")
    List<AttStBase> selectStationByType(String stType);

    @DS("business")
    @Select("select * from st_zqrl_b where mgstcd=#{stcd} and bgtm=(select max(st_zqrl_b.bgtm) from st_zqrl_b where mgstcd=#{stcd})")
    @ResultType(StZqrlB.class)
    List<StZqrlB> selectNewestZqCurveByStcd(@Param("stcd") String stcd);
}
