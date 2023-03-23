package com.ygkj.water.project.strategy;

import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("tideStrategy")
public class TideStrategy implements Strategy{

    private static final String TYPE = "潮位";

    private StationMapper stationMapper;

    private WaterAndRainMapper waterAndRainMapper;

    public TideStrategy(@Autowired StationMapper stationMapper,
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
        List<StTideR> tides = waterAndRainMapper.candidateTide(judgeTideTable(), Arrays.asList(codes.split(",")));
        Map<String, Double> map = tides.stream().collect(Collectors.toMap(StTideR::getMgstcd, StTideR::getTdz));
        for (AttStBase attStBase : attStBases) {
            String stationCode = attStBase.getStCode();
            if (map.containsKey(stationCode)){
                attStBase.setTdz(map.get(stationCode));
            }
        }
        return attStBases;
    }

    private static String judgeTideTable(){
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
        String table = StationType.TIDE.table().concat(year.concat(month));
        return table;
    }
}
