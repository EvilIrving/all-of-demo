package com.ygkj.big.screen.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.big.screen.model.SectionInformation;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.model.StTideR;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author wgf
 * @date 2022/5/19 16:46
 * @Description:
 */
@Repository
public interface SectionInformationMapper extends BaseMapper<SectionInformation> {

    @Select("SELECT a.*,b.st_type,b.guarantee_level,b.warning_level,b.highest_level,b.appear_time FROM section_information a LEFT JOIN att_st_base b ON a.stcd = b.st_code where a.stcd is not null ORDER BY sort ASC")
    List<JSONObject> getSectionInformationStcd();

    @DS("business")
    List<StRiverR> selectRiverByTimeCode(@Param("table") String table, @Param("stcds")List<String> stcds, @Param("dataTime")Date dataTime);

    @DS("business")
    List<StRsvrR> selectRsvrWaterLevelByTimeCode(@Param("table") String table, @Param("stcds")List<String> stcds, @Param("dataTime")Date dataTime);

    @DS("business")
    List<StTideR> selectTideByTimeCode(@Param("table") String table, @Param("stcds")List<String> stcds, @Param("dataTime")Date dataTime);

    @DS("business")
    List<StRiverR> selectRiverHighestLevel(@Param("tables") List<String> tables, @Param("stcds")List<String> stcds,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @DS("business")
    List<StRsvrR> selectRsvrHighestLevel(@Param("tables") List<String> tables, @Param("stcds")List<String> stcds,@Param("startTime") String startTime,@Param("endTime") String endTime);

    @DS("business")
    List<StTideR> selectTideHighestLevel(@Param("tables") List<String> tables, @Param("stcds")List<String> stcds,@Param("startTime") String startTime,@Param("endTime") String endTime);
}
