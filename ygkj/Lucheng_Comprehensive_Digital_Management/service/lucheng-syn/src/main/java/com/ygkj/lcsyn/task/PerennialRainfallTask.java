package com.ygkj.lcsyn.task;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ygkj.lcsyn.mapper.BasicMapper;
import com.ygkj.lcsyn.mapper.SiChuangMapper;
import com.ygkj.lcsyn.model.AttLastYearRainfall;
import com.ygkj.lcsyn.model.AttPerennialRainfall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * @author lxl
 * @create 2022-08-26 16:17
 * @description 常年月平均雨量值定时任务   一年执行一次   获取往年 月雨量数据
 */
@Component
public class PerennialRainfallTask {


    @Autowired
    SiChuangMapper siChuangMapper;

    @Autowired
    BasicMapper basicMapper;


    @Scheduled(cron = "0 0 0 1 1 *")
    public void synTask(){

        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR);
        //查询雨量测站stcd
        List<String> stcds = basicMapper.listAllRainStationCode();

        List<AttPerennialRainfall> rainfallList = siChuangMapper.getAttPerennialRainfallList(year, stcds);
        if (CollectionUtils.isNotEmpty(rainfallList)){
            basicMapper.batchReplacePerennialRainfall(rainfallList);
        }
    }

    //存去年月雨量数据
    @Scheduled(cron = "0 0 0 1 1 *")
    public void synLastYearTask(){

        Calendar calendar = Calendar.getInstance();
        Integer year = calendar.get(Calendar.YEAR) - 1;
        //查询雨量测站stcd
        List<String> stcds = basicMapper.listAllRainStationCode();

        List<AttLastYearRainfall> rainfallList = siChuangMapper.getAttLastYearRainfallList(year, stcds);
        if (CollectionUtils.isNotEmpty(rainfallList)){
            basicMapper.batchAttLastYearRainfall(rainfallList);
        }
    }



}
