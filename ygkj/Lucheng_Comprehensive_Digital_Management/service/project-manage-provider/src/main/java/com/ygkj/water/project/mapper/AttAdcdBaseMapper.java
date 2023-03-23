package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.gragh.model.AttAdcdBase;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AttAdcdBaseMapper extends BaseMapper<AttAdcdBase> {

    @Select("SELECT adcd,adnm FROM att_adcd_base WHERE adcd LIKE ('3303%') AND level = 3")
    List<AttAdcdBase> selectWZCounty();

    @Select("SELECT adcd FROM att_adcd_base\n" +
            "where adcd LIKE '3303%' AND level = 3 AND adnm = #{areaName}")
    String getWenzhouAreaCodeByAdnm(String areaName);

    @Select("SELECT adcd,adnm FROM att_adcd_base WHERE find_in_set(adcd,#{areaCodes})")
    List<AttAdcdBase> selectByAreaCodes(String areaCodes);

    @Select("SELECT adcd,adnm FROM att_adcd_base WHERE adcd LIKE ('330302%')")
    List<AttAdcdBase> selectLCArea();
}
