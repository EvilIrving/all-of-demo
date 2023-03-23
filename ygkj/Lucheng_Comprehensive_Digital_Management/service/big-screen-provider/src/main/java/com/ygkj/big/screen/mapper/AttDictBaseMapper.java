package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.AttDictBase;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/15
 */
@DS("basic")
public interface AttDictBaseMapper {

    @Select("SELECT *" +
            "        FROM att_dict_base\n" +
            "        WHERE del_flag = 0\n" +
            "        AND dict_code = #{dictCode}" +
            "        AND parent_id IS NOT NULL")
    List<AttDictBase> selectDict(String dictCode);
}
