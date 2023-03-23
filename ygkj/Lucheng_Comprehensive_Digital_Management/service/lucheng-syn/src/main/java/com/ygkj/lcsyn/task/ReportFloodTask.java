package com.ygkj.lcsyn.task;

import com.ygkj.lcsyn.mapper.BasicMapper;
import com.ygkj.lcsyn.mapper.BusinessMapper;
import com.ygkj.lcsyn.mapper.WzMapper;
import com.ygkj.lcsyn.model.RsvrReportFlood;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ReportFloodTask {

    @Resource
    private WzMapper wzMapper;

    @Resource
    private BasicMapper basicMapper;

    @Resource
    private BusinessMapper businessMapper;

    /**
     * 从温州同步水库报汛
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void run(){
        //查询所有水库
        List<String> list = basicMapper.listAllResCode();

        //十天内的所有数据
        List<RsvrReportFlood> floodList = wzMapper.list(list);

        //替换
        if(floodList.size() > 0){
            businessMapper.replaceReportFlood(floodList);
        }
    }
}
