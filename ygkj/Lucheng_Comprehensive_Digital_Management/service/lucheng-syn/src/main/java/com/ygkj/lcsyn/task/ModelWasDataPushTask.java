package com.ygkj.lcsyn.task;

import com.ygkj.lcsyn.mapper.DataPushTemporaryMapper;
import com.ygkj.lcsyn.model.PushModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class ModelWasDataPushTask {

    @Resource
    private DataPushTemporaryMapper temporaryMapper;

    //流量站点数据设为0,一小时执行一次
    @Scheduled(cron = "0 0 */1 * * ?")
    //@Scheduled(cron = "0 */1 * * * ?")
    public void syn() throws Exception {
        PushModel data = new PushModel();
        String id =  UUID.randomUUID().toString();
        data.setId(id);
        data.setActualTime(new Date());
        data.setDataValue("0");
        temporaryMapper.addWasData(data);
    }


}
