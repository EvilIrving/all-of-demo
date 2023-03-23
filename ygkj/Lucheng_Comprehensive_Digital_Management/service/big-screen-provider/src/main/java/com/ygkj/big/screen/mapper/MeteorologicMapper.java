package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/4/24
 */
@DS("business")
public interface MeteorologicMapper {
//    @Select("select code from real_typhoon_data order by begin_time desc limit 1")
//    String selectRecentTyphoonCode();

    @Select("select name,code,english_name from real_typhoon_data order by begin_time desc limit 1")
    Map<String, Object> selectRecentTyphoonCode();
}
