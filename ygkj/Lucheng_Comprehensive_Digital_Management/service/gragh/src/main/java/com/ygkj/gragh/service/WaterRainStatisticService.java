package com.ygkj.gragh.service;

import com.ygkj.gragh.vo.response.ChartResVo;

import java.util.List;

/**
 * @author xq
 * @Date 2021/2/23
 */
public interface WaterRainStatisticService {
    Object statisticStationNumByAdcd(String typeString);

    List<ChartResVo<String, Long>> countStTypeNumByAdcdBasin(String adcd, String basin);
}
