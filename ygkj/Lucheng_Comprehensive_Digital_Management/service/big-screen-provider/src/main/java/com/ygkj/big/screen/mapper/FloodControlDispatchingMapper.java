package com.ygkj.big.screen.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.FloodControlDispatchingPlan;
import com.ygkj.gragh.model.StRiverR;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author wgf
 * @date 2022/5/6 10:24
 * @Description:
 */
@Repository
public interface FloodControlDispatchingMapper {

    @Select("SELECT * FROM att_st_base WHERE FIND_IN_SET(st_code,#{stCodes}) AND del_flag = 0")
    List<AttStBase> getStByIds(@Param("stCodes") String stCodes);

    @DS("business")
    List<StRiverR> selectRiverStationWaterLevel(List<String> list, @Param("start") Date start, @Param("end") Date end, @Param("stationCode") String stationCode);

    @Select("SELECT pro.waga_code project_code,pro.waga_name project_name,\n" +
            " pro.start_long longitude,pro.start_lat latitude,\n" +
            " rel.stcd,rel.st_type,wc.water_level control_transport_level,pro.deal_level\n" +
            " FROM\n" +
            " att_waga_base pro\n" +
            " LEFT JOIN sl_proj_st_r rel ON pro.waga_code = rel.project_code AND rel.is_main = 1\n" +
            " LEFT JOIN att_st_base st ON rel.stcd = st.st_code\n" +
            " LEFT JOIN water_level_control wc ON wc.prcd = pro.waga_code AND start_time <= DATE_FORMAT(NOW(), '%m-%d') AND end_Time >= DATE_FORMAT(NOW(), '%m-%d') WHERE pro.waga_code = #{prcd}")
    JSONObject getszinfo(@Param("prcd") String prcd);

    @Select("select tbl.* from (select * from flood_control_dispatching_plan having 1 order by overTime desc) tbl group by tbl.type order by tbl.type")
    List<FloodControlDispatchingPlan> getPlan();

    @Select("select * from flood_control_dispatching_plan where id not in(select * from (select tbl.id from (select * from flood_control_dispatching_plan where del_flag = 0 having 1 order by overTime desc) tbl  group by tbl.type) tb2) order by overTime desc")
    List<FloodControlDispatchingPlan> getHistoryPlan();

    @Select("SELECT id FROM section_configuration WHERE prcd = #{s} AND is_main = 1 LIMIT 1")
    String getDmMain(@Param("s") String s);

    @DS("business")
    @Select("SELECT * FROM section_storm WHERE section_id = #{sectionId} AND tm >= #{date} ORDER BY h2 DESC LIMIT 1")
    JSONObject getMaxH2(@Param("sectionId") String sectionId,@Param("date") String date);
}
