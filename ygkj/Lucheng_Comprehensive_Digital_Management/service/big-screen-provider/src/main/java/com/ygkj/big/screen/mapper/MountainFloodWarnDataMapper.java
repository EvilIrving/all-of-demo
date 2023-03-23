package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.MountainFloodWarnData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 16:07
 */
public interface MountainFloodWarnDataMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/06/18
     **/
    @DS("basic")
    int insert(MountainFloodWarnData entity);


    @DS("business")
    Double findRainDataByCondition(@Param("stcd") String stcd, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("tableName") String tableName);

    @DS("basic")
    List<MountainFloodWarnData> selectPrevTfData(@Param("endTime") String endTime);


}
