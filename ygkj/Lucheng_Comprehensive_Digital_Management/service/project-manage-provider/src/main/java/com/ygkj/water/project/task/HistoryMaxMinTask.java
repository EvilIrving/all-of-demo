package com.ygkj.water.project.task;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.water.project.ProjectManageApplication;
import com.ygkj.water.project.mapper.GiveAnAlarmMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2021/12/24 14:29
 * @Description: 历史最高最低数据更新
 */
/*@SpringBootTest(classes = ProjectManageApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")*/
@Component
@Slf4j
public class HistoryMaxMinTask {

    @Resource
    private GiveAnAlarmMapper giveAnAlarmMapper;

    @PostConstruct
    public void init(){
        updateHistory();
    }

    //@Test
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateHistory(){

        //查询所有测站
        List<JSONObject> list = giveAnAlarmMapper.stationAll();
        Map<String, List<JSONObject>> listMap = list.stream().collect(Collectors.groupingBy(m -> m.getString("stType")));
        //RR水库水位站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZZ河道，PT泵站
        listMap.forEach((k, v)->{
            //暂时只计算潮位站的 最高 最低的潮位 和监测时间
            switch (k){
                case "TT":
                    log.info("开始更新计算潮位测站历史最低最高的数据和时间");
                    //查所有的潮位数据表名
                    List<String> tables = giveAnAlarmMapper.tableNameList("st_tide_r%");
                    for (JSONObject object : v) {
                        String stCode = object.getString("stCode");
                        if (tables != null && tables.size() > 0){
                            //查询最高潮位和时间
                            JSONObject maxTt = giveAnAlarmMapper.getHistoryMax(stCode,tables);
                            //查询最高最低潮位和时间
                            JSONObject minTt = giveAnAlarmMapper.getHistoryMin(stCode,tables);
                            if (maxTt != null){
                                giveAnAlarmMapper.updateMax(maxTt.getString("tm"),maxTt.getString("tdz"), stCode);
                            }
                            if (minTt != null){
                                giveAnAlarmMapper.updateMin(minTt.getString("tm"),minTt.getString("tdz"), stCode);
                            }
                        }
                    }
                    log.info("更新计算潮位测站历史最低最高的数据和时间已结束");
                    break;
                case "RR":
                    //水库水位站
                    break;
                case "DD":
                    //闸坝水位站
                    break;
                case "ZG":
                    //地下水位站
                    break;
                case "ZZ":
                    //河道水位站
                    break;
                case "PT":
                    //泵站
                    break;
                default:
                    break;
            }
        });
    }
}
