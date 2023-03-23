package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.SectionConfiguration;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/10 17:43
 * @Description:
 */
@Repository
public interface SectionConfigurationMapper extends BaseMapper<SectionConfiguration> {

    @DS("business")
    @Select("SELECT TDZ FROM st_astrotd_r WHERE MGSTCD = '06896' AND YMDH = #{nextTime}")
    Double getTwcNextHours(@Param("nextTime") String nextTime);

    @Select("SELECT a.*,b.stcd,c.tide_station_blue_level blueLevel,c.seawall_name FROM section_configuration a LEFT JOIN sl_proj_st_r b ON b.project_code = a.prcd LEFT JOIN att_seawall_base c on a.prcd = c.seawall_code WHERE b.st_type = 'TT' AND b.is_main = 1 AND id = #{id}")
    SectionConfiguration selectSrcdById(@Param("id") String id);

    List<SectionConfiguration> selectSrcdAll(@Param("section") SectionConfiguration section);
}
