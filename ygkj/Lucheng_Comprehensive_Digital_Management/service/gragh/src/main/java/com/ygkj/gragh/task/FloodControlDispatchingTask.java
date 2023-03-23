package com.ygkj.gragh.task;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.mapper.FloodControlDispatchingPlanMapper;
import com.ygkj.gragh.mapper.SynRainAndWaterMapper;
import com.ygkj.gragh.model.FloodControlDispatchingPlan;
import com.ygkj.gragh.model.StRiverR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FloodControlDispatchingTask {
    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
    @Autowired
    private SynRainAndWaterMapper synRainAndWaterMapper;
    @Autowired
    private FloodControlDispatchingPlanMapper dispatchingPlanMapper;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void syn(){
        String riverTable = StationType.RIVER.table().concat(LocalDate.now().format(yyyyMM));
        List<StRiverR>  list = synRainAndWaterMapper.selectRiverByTimeCode(riverTable,new ArrayList<String>(){{add("010111");}},null);
        if (list != null && list.size() > 0){
            JSONObject object = dispatchingPlanMapper.kyWaterLevel("3350ee98-1c7b-11ea-8760-6c92bf66b51e");
            StRiverR stRiverR = list.get(0);
            if (stRiverR.getZ() > object.getDouble("water_level")){
                FloodControlDispatchingPlan dispatchingPlan = new FloodControlDispatchingPlan();
                dispatchingPlan.setOvertime(LocalDateTime.now().format(dtf));
                dispatchingPlan.setType("3");
                dispatchingPlanMapper.insert(dispatchingPlan);
            }
        }
    }
}
