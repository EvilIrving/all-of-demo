package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


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


    @DS("business")
    List<Map<String, Object>> getTideLevels(@Param("tableName") String tableName, @Param("set") Set<String> set);

    @DS("business")
    List<Map<String, Object>> getYbTideLevels(@Param("set") HashSet<String> set);


    /**
     * 查询水闸工程对应的主测站
     * @param prcd
     * @return
     */
    List<JSONObject> getDdStation(@Param("prcd") List<String> prcd);

    @DS("business")
    List<JSONObject> getDdStationReal(@Param("stcd")List<String> stcd, @Param("tableName") String tableName);

    List<JSONObject> getPtStation(@Param("prcd")List<String> prcd);

    List<JSONObject> getPust(@Param("prcd")List<String> prcd);

    @Select("select st_code stCode,st_type stType from att_st_base")
    List<JSONObject> stationAll();

    @DS("business")
    @Select("SHOW TABLES LIKE #{tableName}")
    List<String> tableNameList(@Param("tableName")String tableName);

    @DS("business")
    JSONObject getHistoryMax(@Param("stCode")String stCode, @Param("tables")List<String> tables);

    @DS("business")
    JSONObject getHistoryMin(@Param("stCode")String stCode, @Param("tables")List<String> tables);

    @Update("UPDATE att_st_base SET highest_level = #{tdz}, appear_time = #{tm} WHERE st_code = #{stCode}")
    void updateMax(@Param("tm")String tm, @Param("tdz")String tdz, @Param("stCode")String stCode);

    @Update("UPDATE att_st_base SET record_low = #{tdz}, low_time = #{tm} WHERE st_code = #{stCode}")
    void updateMin(@Param("tm")String tm, @Param("tdz")String tdz, @Param("stCode")String stCode);
}
