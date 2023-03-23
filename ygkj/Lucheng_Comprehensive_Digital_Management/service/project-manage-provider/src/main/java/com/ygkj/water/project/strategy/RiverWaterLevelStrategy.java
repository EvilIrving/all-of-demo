package com.ygkj.water.project.strategy;

import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("riverWaterLevelStrategy")
public class RiverWaterLevelStrategy implements Strategy{

    private static final String TYPE = "河道水位";

    private StationMapper stationMapper;

    private WaterAndRainMapper waterAndRainMapper;

    public RiverWaterLevelStrategy(@Autowired StationMapper stationMapper,
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
        List<StRiverR> riverWaterLevels = waterAndRainMapper.candidateRiverWaterLevel(judgeRiverWaterLevelTable(), Arrays.asList(codes.split(",")),10);
        Map<String, Double> map = riverWaterLevels.stream().collect(Collectors.toMap(StRiverR::getMgstcd, StRiverR::getZ));
        for (AttStBase attStBase : attStBases) {
            String stationCode = attStBase.getStCode();
            if (map.containsKey(stationCode)){
                attStBase.setWaterLevel(map.get(stationCode));
            }
        }
        return attStBases;
    }

    private static String judgeRiverWaterLevelTable(){
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
        String table = StationType.RIVER.table().concat(year.concat(month));
        return table;
    }
}
