package com.ygkj.big.screen.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.big.screen.model.AttStBase;
import com.ygkj.big.screen.model.HeavyRainsArchive;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/6 13:54
 * @Description:
 */
@Repository
public interface HeavyRainsArchiveMapper extends BaseMapper<HeavyRainsArchive> {

    @Select("select * from att_st_base where st_type = #{stType} AND del_flag = 0")
    List<AttStBase> getTypeStcd(@Param("stType") String stType);

    @DS("business")
    String getAvgSumDrp(@Param("tableNames") List<String> tableNames, @Param("heavyRainsArchive") HeavyRainsArchive heavyRainsArchive);

    @DS("business")
    List<JSONObject> getAvgHourDrp(List<String> tableNames, HeavyRainsArchive heavyRainsArchive);

    @Select("select * from att_st_base where is_rain_station = #{isRainStation} AND del_flag = 0")
    List<AttStBase> getRainStcd(@Param("isRainStation") String isRainStation);
}
