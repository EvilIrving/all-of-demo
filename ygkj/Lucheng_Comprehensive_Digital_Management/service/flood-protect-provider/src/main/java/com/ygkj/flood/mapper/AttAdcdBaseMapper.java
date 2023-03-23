package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.AttAdcdBase;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/4/30
 */
@DS("basic")
public interface AttAdcdBaseMapper {

    @Select("select adcd,adnm from att_adcd_base where FIND_IN_SET(adcd,#{adcds})")
    List<AttAdcdBase> selectAdcdNameByAdcds(String adcds);

    @Select("SELECT adcd,adnm FROM att_adcd_base WHERE adcd LIKE ('3303%') AND level = 3")
    List<AttAdcdBase> selectCountyOfWz();

    @Select("select adcd from att_adcd_base where adcd LIKE '3303%' AND level = 3 AND adnm like concat('%',#{adnm},'%') limit 1")
    String selectWzAdcdByAdnm(String adnm);


}
