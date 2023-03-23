package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.SynRainAndWaterMapper;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.service.YongjiaSynchronizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YongjiaSynchronizedServiceImpl implements YongjiaSynchronizedService {

    private SynRainAndWaterMapper synRainAndWaterMapper;

    public YongjiaSynchronizedServiceImpl(@Autowired SynRainAndWaterMapper synRainAndWaterMapper) {
        this.synRainAndWaterMapper = synRainAndWaterMapper;
    }

    @Override
    public boolean synchronizeData(YongjiaMetaData metaData) {
//        List<StPptnR> rainList = new ArrayList<>();
//        List<StRsvrR> waterList = new ArrayList<>();
        List<YongjiaRain> rains = metaData.getRains();
        List<YongjiaWater> waters = metaData.getWaters();
        if (!rains.isEmpty()) {
            for (YongjiaRain rain : rains) {
                StPptnR stPptnR = new StPptnR();
                stPptnR.setMgstcd(rain.getStcdt());
                stPptnR.setStcd(rain.getStcdt());
                stPptnR.setDrp(Double.parseDouble(rain.getDtrn()));
                stPptnR.setTm(rain.getYmdhm());
//                rainList.add(stPptnR);
                synRainAndWaterMapper.insertRainfall(stPptnR);
            }
        }
        if (!waters.isEmpty()) {
            for (YongjiaWater water : waters) {
                StRsvrR stRsvrR = new StRsvrR();
                stRsvrR.setMgstcd(water.getStcdt());
                stRsvrR.setStcd(water.getStcdt());
                stRsvrR.setRz(water.getZi().doubleValue());
                stRsvrR.setTm(water.getYmdhm());
                synRainAndWaterMapper.insertReservoirWaterLevel(stRsvrR);
            }
        }
        return true;
    }
}
