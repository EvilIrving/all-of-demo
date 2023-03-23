package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttAdcdBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description 行政区划查询
 * @Date 2021/1/14
 */
@DS("basic")
public interface AttAdcdBaseMapper {

    List<AttAdcdBase> selectAdnmByAdcdList(@Param("adcdList") List<String> adcdList);

    @Select("SELECT adcd,adnm FROM att_adcd_base WHERE adcd LIKE ('3303%') AND level = 3")
    List<AttAdcdBase> selectWZCounty();

    String selectAdnmByAdcd(String adcd);
}
