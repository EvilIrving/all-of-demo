package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.AttRnBase;
import com.ygkj.big.screen.model.AttStBase;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/4/28
 */
@DS("basic")
public interface AttStBaseMapper {
    List<AttStBase> selectByCondition(Map<String, Object> param);

    Integer countByCondition(Map<String, Object> param);

    @Select("select distinct st_code from att_st_mark_base where mark=#{mark}")
    @ResultType(String.class)
    List<String> selectStcdByMark(String mark);

    /**
     * 查询河网
     *
     * @param param
     * @return
     */
    List<AttRnBase> selectRiverNetwork(Map<String, Object> param);
}
