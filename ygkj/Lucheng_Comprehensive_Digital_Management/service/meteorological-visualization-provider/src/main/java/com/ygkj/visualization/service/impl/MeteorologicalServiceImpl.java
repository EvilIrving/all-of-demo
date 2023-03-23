package com.ygkj.visualization.service.impl;

import com.ygkj.visualization.mapper.MeteorologicalMapper;
import com.ygkj.visualization.model.AttMeteorologicalStBase;
import com.ygkj.visualization.model.StMeR;
import com.ygkj.visualization.service.MeteorologicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("meteorologicalService")
public class MeteorologicalServiceImpl implements MeteorologicalService {

    private MeteorologicalMapper meteorologicalMapper;

    public MeteorologicalServiceImpl(@Autowired MeteorologicalMapper meteorologicalMapper) {
        this.meteorologicalMapper = meteorologicalMapper;
    }

    @Override
    public List<AttMeteorologicalStBase> meteorologicalRealTimeData() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,-3);
        List<StMeR> list = meteorologicalMapper.newestMeteorologicData(format.format(instance.getTime()));
        Map<String, StMeR> map = list.stream().collect(Collectors.toMap(StMeR::getMgstcd, Function.identity()));
        List<AttMeteorologicalStBase> stations = meteorologicalMapper.meteorologicalStations();
        for (AttMeteorologicalStBase station : stations) {
            station.setMeteorological(map.getOrDefault(station.getStationCode(),null));
        }
        return stations;
    }

    @Override
    public List<StMeR> meteorologicalPastTimeData(String stationCode,Integer days) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,-1*days);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        String startTime = format.format(instance.getTime());
        return meteorologicalMapper.pastMeteorologicData(stationCode,startTime);
    }
}
