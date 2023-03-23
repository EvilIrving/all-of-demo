package com.ygkj.visualization.service;

import com.ygkj.visualization.model.AttMeteorologicalStBase;
import com.ygkj.visualization.model.StMeR;

import java.util.List;

public interface MeteorologicalService {

    List<AttMeteorologicalStBase> meteorologicalRealTimeData();

    List<StMeR> meteorologicalPastTimeData(String stationCode,Integer days);
}
