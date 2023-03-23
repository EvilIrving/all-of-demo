package com.ygkj.river.mapper;

import com.ygkj.river.vo.request.AttWaterAreaReqVo;
import com.ygkj.river.vo.response.AttWaterAreaRespVo;
import com.ygkj.river.vo.response.HistogramWaterArea;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AttWaterAreaMapper {

    BigDecimal totalStatisticsWaterAreaByYear(@Param("year") Integer year);

    /**
     * 获取数据中所有的年份
     *
     * @return
     */
    List<Integer> calYears();

    List<AttWaterAreaRespVo> loadPage(AttWaterAreaReqVo vo);

    List<HistogramWaterArea> histogramWaterArea(@Param("year") Integer year);

    List<AttWaterAreaRespVo> waterAreaEachAdministrative(@Param("year") Integer year);

    int updateData(AttWaterAreaRespVo entity);

    AttWaterAreaRespVo findByAreaNameAndYear(@Param("areaName") String areaName, @Param("year") String year);
}
