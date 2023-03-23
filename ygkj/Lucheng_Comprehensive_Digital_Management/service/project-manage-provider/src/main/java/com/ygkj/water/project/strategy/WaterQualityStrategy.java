package com.ygkj.water.project.strategy;

import com.ygkj.gragh.model.AttBswqBase;
import com.ygkj.gragh.vo.response.BswqResVo;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("waterQualityStrategy")
public class WaterQualityStrategy implements Strategy{

    private static final String TYPE = "水质";

    private StationMapper stationMapper;

    private WaterAndRainMapper waterAndRainMapper;

    public WaterQualityStrategy(@Autowired StationMapper stationMapper,
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
        List<AttBswqBase> qualityStations = stationMapper.candidateWaterQualityStations(codes);
        List<BswqResVo> waterQualities = waterAndRainMapper.candidateWaterQuality(Arrays.asList(codes.split(",")));
        Map<String, BswqResVo> map = waterQualities.stream().collect(Collectors.toMap(BswqResVo::getCode, bswqResVo -> bswqResVo));
        for (AttBswqBase station : qualityStations) {
            String code = station.getCode();
            if (map.containsKey(code)){
                station.setWaterQuality(map.get(code));
            }
        }
        return qualityStations;
    }
}
