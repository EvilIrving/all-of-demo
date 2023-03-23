package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.vo.response.CollectPointRainfall;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xq
 * @Date 2021/2/26
 */
@DS("business")
public interface CollectPointMapper {

    List<CollectPointRainfall> selectAllPoint(@Param("userId") String userId, @Param("name") String name);

}
