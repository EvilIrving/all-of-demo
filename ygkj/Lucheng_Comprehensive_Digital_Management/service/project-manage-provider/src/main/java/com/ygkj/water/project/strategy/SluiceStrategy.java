package com.ygkj.water.project.strategy;

import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("sluiceStrategy")
public class SluiceStrategy implements Strategy{

    private static final String TYPE = "工情";

    private StationMapper stationMapper;

    private WaterAndRainMapper waterAndRainMapper;

    public SluiceStrategy(@Autowired StationMapper stationMapper,
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
        List<StWasR> sluiceWaterLevels = waterAndRainMapper.candidateSluiceWaterLevel(judgeSluiceTable(), Arrays.asList(codes.split(",")));
        Map<String, StWasR> map = sluiceWaterLevels.stream().collect(Collectors.toMap(StWasR::getMgstcd, stWasR -> stWasR));
        for (AttStBase attStBase : attStBases) {
            String stationCode = attStBase.getStCode();
            if (map.containsKey(stationCode)){
                StWasR stWasR = map.get(stationCode);
                BeanUtils.copyProperties(stWasR,attStBase);
            }
        }
        return attStBases;
    }

    private static String judgeSluiceTable(){
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
        String table = StationType.GATE.table().concat(year.concat(month));
        return table;
    }
}
