package com.ygkj.big.screen.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.response.AttWagaBaseVo;
import com.ygkj.big.screen.vo.response.WagaScattergramVo;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.project.vo.request.BmCtroChcuVo;
import com.ygkj.project.vo.request.BmCtroChwlIndexVo;
import com.ygkj.project.vo.response.AttResBaseVo;
import com.ygkj.project.vo.response.SeawallPointResVo;
import com.ygkj.visualization.model.RealTyphoonData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wgf
 * @date 2021/10/18 19:33
 * @Description:
 */
public interface GiveAnAlarmMapper {

    List<AttSeawallBase> getseawall(String seawallCode);

    @DS("business")
    List<Map<String, Object>> getTideLevels(@Param("tableName") String tableName,@Param("set") Set<String> set, @Param("time")String time);

    @DS("business")
    List<Map<String, Object>> getYbTideLevels(@Param("set")HashSet<String> set, @Param("time")String time);


    @DS("business")
    List<JSONObject> getSectionStorm(@Param("time")String time);

    @DS("business")

    @Select("SELECT id,name, begin_time startTime, points FROM real_typhoon_data WHERE end_time is NULL")
    List<Map<String, Object>> typhoonGiveAnAlarm();

    @Select("SELECT st_code stcd, st_name stName,st_long,st_lat,st_loc FROM att_st_base WHERE st_type = 'PP' AND area_code like '330302%' AND del_flag = 0")
    List<Map<String, Object>> getRainLevels();

    @DS("business")
    List<Map<String, Object>> getRains(@Param("tableName") String tableName,@Param("set") Set<String> set);

    List<Map<String, Object>> getHoursRain(@Param("time")String time, @Param("set")Set<String> set);

    @Select("SELECT * FROM att_st_base WHERE st_type IN ('RR','TT','ZZ','DD') AND del_flag = 0")
    List<AttStBase> getwaterLevels();

    @DS("business")
    RealTyphoonData selectDetail(@Param("code") String code);

    @DS("business")
    List<RealTyphoonData> getTyphoonByDate(@Param("date") String date);

    @DS("business")
    @Select("select * FROM real_typhoon_data WHERE id = #{id}")
    RealTyphoonData selectTyphoonById(@Param("id")String id);

    @Select("SELECT * FROM avoidance_point WHERE longitude IS NOT NULL AND latitude IS NOT NULL")
    List<AvoidancePoint> getAvoidancePoint();

    @Select("SELECT * FROM att_warehouse_base WHERE longitude IS NOT NULL AND latitude IS NOT NULL")
    List<AttWarehouseBase> getAttWarehouseBase();

    @Select("SELECT * FROM att_team_base WHERE longitude IS NOT NULL AND latitude IS NOT NULL")
    List<AttTeamBase> getAttTeamBase();

    @Select("select * from seawall_range where prcd = #{seawallCode} AND type = #{type}")
    List<SeawallRange> getSeawallRange(@Param("seawallCode")String seawallCode, @Param("type")int type);

    List<WagaScattergramVo> getStationData(@Param("list")List<String> list);

    List<AttWagaBaseVo> getWageData(List<String> list);

    List<Map<String, Object>> listFcstVal(@Param("fcstId")String fcstId,@Param("time") String time,@Param("set") Set<String> set);

    @Select("SELECT longitude,latitude FROM att_seawall_base WHERE seawall_code = #{seawallCode}")
    SeawallPointResVo selectSeawallPoint(String seawallCode);

    @Select("SELECT * FROM water_level_control WHERE start_time <= DATE_FORMAT(NOW(), '%m-%d') AND end_Time >= DATE_FORMAT(NOW(), '%m-%d') ")
    List<JSONObject> kyWaterLevel();

    @Select("SELECT * FROM att_st_base WHERE del_flag = 0 AND st_type=#{stType}")
    List<AttStBase> selectStationByType(String stType);

    @DS("business")
    @Select("SHOW TABLES LIKE #{tableName}")
    String checkTableExist(@Param("tableName") String tableName);

    @DS("business")
    List<Map<String, Object>> getCwsqList(@Param("cwTables")List<String> cwTables, @Param("codes") List<String> codes, @Param("startTime")String startTime, @Param("endTime")String endTime);

    @DS("business")
    List<Map<String, Object>> getHourRain(@Param("tableName") String tableName,@Param("hour") Double hour,@Param("set") Set<String> set);

    @DS("business")
    List<Map<String, Object>> getHdsqList(@Param("hdTables")List<String> hdTables, @Param("hdCodes")List<String> hdCodes, @Param("startTime")String startTime, @Param("endTime")String endTime);

    @DS("business")
    List<Map<String, Object>> getSzsqList(List<String> szTables, List<String> szCodes, String startTime, String endTime);

    @Select("SELECT bcc.id, bcc.X, bcc.Y FROM BM_CTRO_CHCU bcc " +
            "left join bm_ctro_chwl_index bcci on bcci.id=bcc.curcd " +
            "WHERE bcci.PRCD= #{vo.prcd} and bcci.Curtp= #{vo.curtp} order by bcc.Y ")
    List<BmCtroChcuVo> getBmCtroChcuList(@Param("vo") BmCtroChwlIndexVo vo);

    @Select("SELECT a.res_code,a.res_name,a.fl_low_lim_lev_cap,a.safety_conclusion,a.norm_wat_lev,a.norm_pool_stag_cap,a.yphoon_flood_llimit_water_level,b.stcd,c.warning_level,c.limit_level FROM att_res_base a " +
            "LEFT JOIN sl_proj_st_r b ON a.res_code = b.project_code " +
            "LEFT JOIN att_st_base c ON b.stcd = c.st_code WHERE a.del_flag = 0 AND b.is_main = 1")
    List<AttResBaseVo> getSkListStcd();

    @DS("business")
    List<Map<String, Object>> getsksqList(@Param("skTables")List<String> skTables, @Param("skCodes")List<String> skCodes, @Param("startTime")String startTime, @Param("endTime")String endTime);

    @Select("select * from att_adcd_base where del_flag = 0")
    List<AttAdcdBase> getAdcdList();

    @Insert("INSERT INTO flood_control_and_presentation (`id`, `tm`, `file_id`, `type`) VALUES (#{id}, #{endTime}, #{fileId}, #{type})")
    void saveResponseLevel(@Param("id")String id, @Param("endTime")String endTime, @Param("fileId")String fileId, @Param("type")String type);

    @Select("SELECT COUNT(1) FROM flood_control_and_presentation WHERE tm = #{endTime} AND type = 1")
    int isFloodControlPresentation(@Param("endTime")String endTime);

    @Select("SELECT a.res_code,a.res_name,a.fl_low_lim_lev_cap,a.norm_wat_lev,a.norm_pool_stag_cap,a.yphoon_flood_llimit_water_level,b.stcd,c.warning_level,c.limit_level FROM att_res_base a \n" +
            "            LEFT JOIN sl_proj_st_r b ON a.res_code = b.project_code \n" +
            "            LEFT JOIN att_st_base c ON b.stcd = c.st_code WHERE a.del_flag = 0 AND b.is_main = 1\n" +
            "UNION ALL\t\t\t\t\t\t\n" +
            "SELECT '' res_code, st_name res_name,'' fl_low_lim_lev_cap,'' norm_wat_lev,'' norm_pool_stag_cap,'' yphoon_flood_llimit_water_level,st_code stcd,warning_level,limit_level FROM att_st_base  WHERE st_code IN ('03189','01588','06725')")
    List<AttResBaseVo> getWaterListStcd();


    @Select("SELECT * FROM att_st_base WHERE del_flag = 0 AND is_rain_station = '1'")
    List<AttStBase> getRainStationAll();

    /**
     * 时间范围内统计传入测站的累计雨量数据
     * @param start 开始时间
     * @param end 结束时间
     * @param tables 表名
     * @param stCodes 查询的测站编码
     * @return
     */
    @DS("business")
    List<JSONObject> getRainsSum(@Param("start") String start, @Param("end")String end, @Param("tables")List<String> tables, @Param("stCodes")List<String> stCodes);


    /**
     * 常年同期累计雨量数据
     * @param start 开始的月份
     * @param end 结束的月份
     * @param stCodes 查询的测站编码
     * @return
     */
    List<JSONObject> getLastRainsSum(@Param("start") Integer start, @Param("end")Integer end, @Param("stCodes")List<String> stCodes);

    /**
     * 去年同期累计雨量数据
     * @param start 开始的月份
     * @param end 结束的月份
     * @param stCodes 查询的测站编码
     * @return
     */
    List<JSONObject> getLastYearRainsSum(@Param("start") Integer start, @Param("end")Integer end, @Param("stCodes")List<String> stCodes);

    /**
     * 查询本月降水天数
     * @param table
     * @param stCodes
     * @return
     */
    @DS("business")
    Integer getJyDayCount(@Param("table") String table, @Param("stCodes")List<String> stCodes);

    /**
     * 本月有叫明显降雨的天（当天测站累计雨量 >=30的），返回时间和当天最大的雨量测站
     * @param table
     * @param stCodes
     * @return
     */
    @DS("business")
    List<JSONObject> getJmxjyList(@Param("table") String table, @Param("stCodes")List<String> stCodes);

    @Select("select st.*,adcd.adnm adName from att_st_base st left join att_adcd_base adcd on st.area_code = adcd.adcd where st.is_rain_station='1' and st.del_flag = '0'  order by st.area_code asc")
    List<AttStBase> getRainStation();

    @DS("business")
    List<Map<String, Object>> getRainsqlList(@Param("rainTables") List<String> rainTables, @Param("stCodes") List<String> stCodes,
                                             @Param("startTime") String startTime, @Param("endTime") String endTime,
                                             @Param("minRain") Double minRain, @Param("maxRain") Double maxRain);

    @Select("select st.st_name,st.st_code,bm.`NAME` from  att_st_base st left join sl_proj_st_r sl on st.st_code=sl.stcd left join bm_project bm on sl.project_code=bm.PRCD\n" +
            "where st.st_type='SM'")
    List<JSONObject> getMonitoringPoint();
}
