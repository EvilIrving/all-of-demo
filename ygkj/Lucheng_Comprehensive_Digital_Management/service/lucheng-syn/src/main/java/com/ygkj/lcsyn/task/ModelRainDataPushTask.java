package com.ygkj.lcsyn.task;

import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.mapper.DataPushQueryMapper;
import com.ygkj.lcsyn.mapper.DataPushTemporaryMapper;
import com.ygkj.lcsyn.model.SimulationPoint;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
鹿城戍浦江模型算法雨量推送任务,每十分钟执行一次获取最新数据
*/
@Component
@Slf4j
public class ModelRainDataPushTask {
    @Resource
    private DataPushTemporaryMapper temporaryMapper;
    @Resource
    private DataPushQueryMapper dataPushQueryMapper;

    @Scheduled(cron = "0 0 */1 * * ?")
    // @Scheduled(cron = "0 */1 * * * ?")
    public void syn() throws Exception {
        String tableName =  "st_pptn_r_" + DateUtils.formatDate(new Date(),"yyyyMM");

        List<SimulationPoint> pointList = temporaryMapper.queryBasinPoints();
        Map<String,List<SimulationPoint>> map = pointList.stream().collect(Collectors.groupingBy(SimulationPoint :: getPointBasin));
        for(String basin : map.keySet()){
            double allDrp = 0.0;
            List<SimulationPoint> list = map.get(basin);
            for(SimulationPoint point  : list){
                String drp = dataPushQueryMapper.queryPointData(tableName,point.getPointCode());
                if(StringUtils.isEmpty(drp)){
                    drp = "0.0";
                }
                allDrp = allDrp + Double.parseDouble(drp);
            }
            double avgDrp = allDrp/list.size();
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GTM-8"));
            calendar.setTime(new Date());

            if("1".equals(basin)){
                //流域1
                temporaryMapper.addBasin1(UUID.randomUUID().toString(),calendar.getTime(),avgDrp);
            }else if("2".equals(basin)){
                //流域2
                temporaryMapper.addBasin2(UUID.randomUUID().toString(),calendar.getTime(),avgDrp);
            }
        }
    }

}
