package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.gragh.model.StAstrotdR;
import com.ygkj.project.model.WindSpeedRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@DS("business")
public interface WindSpeedMapper extends BaseMapper<WindSpeedRecord> {

    /**
     * 批量替换
     *
     * @param list
     */
    void replaceAll(List<WindSpeedRecord> list);

    /**
     * 时间范围内的天文潮数据
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<StAstrotdR> listAstrotd(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
