package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.RaderData;
import com.ygkj.digitization.vo.response.ResSoftResVo;
import com.ygkj.digitization.vo.response.SeawallSoftResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@DS("business")
public interface BusinessMapper {

    /**
     * 最新水位
     *
     * @param tableName
     * @param set
     * @return
     */
    List<ResSoftResVo> lastRz(@Param("tableName") String tableName, @Param("collection") Set<String> set);

    /**
     * 最新潮位
     *
     * @param tableName
     * @param set
     * @return
     */
    List<SeawallSoftResVo> lastTide(@Param("tableName") String tableName, @Param("collection") Set<String> set);

    @Select("SELECT monitor_time,image_url AS url FROM rader_data WHERE monitor_time BETWEEN #{startTime} AND #{endTime}")
    List<RaderData> raderData(String startTime,String endTime);
}
