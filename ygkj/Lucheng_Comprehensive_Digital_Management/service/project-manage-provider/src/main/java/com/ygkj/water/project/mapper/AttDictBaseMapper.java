package com.ygkj.water.project.mapper;

import com.ygkj.project.model.AttDictBase;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/5/7
 */
public interface AttDictBaseMapper {
    @Select("select * from att_dict_base where dict_code=#{dictCode} and parent_id is not null")
    List<AttDictBase> selectByDictCode(String dictCode);
}
