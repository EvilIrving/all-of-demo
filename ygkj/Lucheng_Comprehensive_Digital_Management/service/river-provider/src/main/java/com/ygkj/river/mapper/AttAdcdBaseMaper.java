package com.ygkj.river.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.river.model.AttAdcdBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/4/7
 */
@DS("basic")
public interface AttAdcdBaseMaper extends BaseMapper<AttAdcdBase> {

    @Select("SELECT adcd,adnm\n" +
            "    FROM att_adcd_base\n" +
            "    where del_flag=0 and parent_adcd='330300000000'\n" +
            "    AND adcd !='330351000000'\n" +
            "    AND adcd !='330353000000'\n" +
            "    AND adcd !='330354000000'\n" +
            "    AND adcd !='330355000000'")
    List<AttAdcdBase> countyOfWenZhou();

    @Select("select adnm from att_adcd_base where adcd=#{adcd}")
    String selectNameOfAdcd(@Param("adcd") String adcd);


    @Select("select adcd,adnm from att_adcd_base where FIND_IN_SET(adcd,#{adcds})")
    List<Map<String, String>> selectNamesOfAdcds(@Param("adcds") String adcds);


    @Select("select * from att_adcd_base where parent_adcd = '330300000000' order by order_flag")
    List<AttAdcdBase> getWenZhouAdcdList();
}
