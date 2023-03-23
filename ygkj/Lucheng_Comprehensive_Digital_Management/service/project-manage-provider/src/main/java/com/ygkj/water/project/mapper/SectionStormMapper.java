package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.project.model.SectionStorm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@DS("business")
public interface SectionStormMapper extends BaseMapper<SectionStorm> {

    /**
     * 批量替换
     *
     * @param list
     */
    void replaceAll(List<SectionStorm> list);

    @Select("select * from section_storm where section_id = #{id} order by tm desc limit 24")
    List<SectionStorm> select24HourData(@Param("id") String dmId);

    @Select("select * from lc_business_db.section_storm where section_id = #{id} and TM > DATE_SUB(CURDATE(), interval 3 DAY) order by tm DESC")
    List<SectionStorm> select3DaysData(@Param("id") String dmId);

    @Select("SELECT a.section_id, a.tm, a.h2 FROM (SELECT * FROM section_storm  WHERE tm >= #{date} ORDER BY h2 DESC) a GROUP BY a.section_id")
    List<JSONObject> getMaxH2ByTime(@Param("date") Date date);

    List<StTideR> selectHourTide(@Param("list") List<String> list, @Param("start") Date start, @Param("end") Date end, @Param("stationCode") String stationCode);

}
