package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.StBase;
import com.ygkj.flood.vo.request.StQueryReqVo;
import com.ygkj.flood.vo.response.WarningStationResVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface StationMapper {

    WarningStationResVo load(String stationCode);

    /**
     * 查询水库测站
     *
     * @return
     */
    @Select("select a.st_code,a.st_name,a.st_type,a.st_long,a.st_lat," +
            " a.limit_level,a.flood_level,a.guarantee_level,a.warning_level,a.proj_code,a.area_code,a.bas" +
            " from att_st_base as a inner join att_res_base as b on a.proj_code=b.res_code and b.del_flag=0 where a.st_type=#{stType}")
    List<StBase> loadRsvrSt(String stType);

    List<StBase> loadStByCondtion(StQueryReqVo reqVo);


    List<StBase> loadStByMark(String mark, String stName);

    @DS("basic")
    @Select("select a.st_code,b.res_name,b.low_left_long,b.low_left_lat,b.res_code,b.mei_flood_limit_water_level from (select st_code,proj_code from att_st_base where res_grade='大中型水库') as a left JOIN att_res_base as b on a.proj_code=b.res_code")
    List<Map<String, Object>> selectLargeMidResData();
}
