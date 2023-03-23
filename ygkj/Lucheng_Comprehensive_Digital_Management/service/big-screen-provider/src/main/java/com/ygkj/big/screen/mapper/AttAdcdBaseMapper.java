package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.AttAdcdBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/23
 */
@DS("basic")
public interface AttAdcdBaseMapper {
    @Select("select * from att_adcd_base where parent_adcd='330300000000'")
    List<AttAdcdBase> queryAllAdOfWz();

    /**
     * 温州下辖除市本级以外行政区划
     */
    @Select("select adcd,adnm from att_adcd_base where parent_adcd='330300000000' and find_in_set(adcd,'330301000000')=0")
    List<AttAdcdBase> adcdAdnmCountyOfWzExcludeCityLevel();

    @Select("select adcd,adnm from att_adcd_base where FIND_IN_SET(adcd,#{adcds})")
    List<AttAdcdBase> selectAdnmByAdcdList(@Param("adcds") String adcds);

    @Select("select adnm from att_adcd_base where adcd = #{adcd}")
    String selectAdnmByAdcd(@Param("adcd") String adcd);
}
