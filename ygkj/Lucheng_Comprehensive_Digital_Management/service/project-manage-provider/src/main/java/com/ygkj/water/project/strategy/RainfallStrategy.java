package com.ygkj.water.project.strategy;

import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StPptnR;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("rainfallStrategy")
public class RainfallStrategy implements Strategy {

    private static final String TYPE = "雨情";

    private StationMapper stationMapper;

    private WaterAndRainMapper waterAndRainMapper;

    public RainfallStrategy(@Autowired StationMapper stationMapper,
                            @Autowired WaterAndRainMapper waterAndRainMapper) {
        this.stationMapper = stationMapper;
        this.waterAndRainMapper = waterAndRainMapper;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection records(String codes) {
        List<AttStBase> attStBases = stationMapper.candidateStation(codes);
        List<StPptnR> rainfalls = waterAndRainMapper.candidateRainfall(judgeRainfallTable(), Arrays.asList(codes.split(",")));
        Map<String, Double> map = rainfalls.stream().collect(Collectors.toMap(StPptnR::getMgstcd, StPptnR::getDrp));
        for (AttStBase attStBase : attStBases) {
            String stationCode = attStBase.getStCode();
            if (map.containsKey(stationCode)){
                attStBase.setRainfall(map.get(stationCode));
            }
        }
        return attStBases;
    }

    private static String judgeRainfallTable(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        int monthIndex = calendar.get(Calendar.MONTH) + 1;
        String month;
        if (monthIndex < 10){
            month = "0"+monthIndex;
        } else {
            month = String.valueOf(monthIndex);
        }
        String table = StationType.RAINFALL.table().concat(year.concat(month));
        return table;
    }
}
