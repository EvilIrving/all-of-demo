package com.ygkj.lcsyn.task;



import com.ygkj.lcsyn.mapper.DataPushQueryMapper;
import com.ygkj.lcsyn.mapper.DataPushTemporaryMapper;
import com.ygkj.lcsyn.model.PushModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 鹿城戍浦江模型算法水位推送任务,每五分钟执行一次获取最新数据
 */
@Component
@Slf4j
public class ModelRiverDataPushTask {
    @Resource
    private DataPushTemporaryMapper temporaryMapper;

    @Resource
    private DataPushQueryMapper dataPushQueryMapper;

    //水位站点固定取 戍浦江河口 010111
    @Scheduled(cron = "0 */5 * * * ?")
    public void syn() throws Exception {
        String tableName =  "st_river_r_" + DateUtils.formatDate(new Date(),"yyyyMM");
        //实时数据最近的一次数据
        PushModel data = dataPushQueryMapper.queryLastData(tableName,"010111");
        //已有的最近一条数据
        PushModel lastRiverData = temporaryMapper.queryMaxRiverData();
        if(lastRiverData != null &&  lastRiverData.getActualTime().getTime() >= data.getActualTime().getTime()){

        }else{
            String id =  UUID.randomUUID().toString();
            data.setId(id);
            temporaryMapper.addRiverData(data);
        }



    }




}
