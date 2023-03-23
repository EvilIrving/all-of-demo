package com.ygkj.water.project.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ygkj.gragh.model.StAstrotdR;
import com.ygkj.project.model.SectionConfiguration;
import com.ygkj.project.model.SectionStorm;
import com.ygkj.project.model.WaveHeight;
import com.ygkj.project.model.WindSpeedRecord;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.water.project.ProjectManageApplication;
import com.ygkj.water.project.mapper.SectionConfigurationMapper;
import com.ygkj.water.project.mapper.SectionStormMapper;
import com.ygkj.water.project.mapper.WindSpeedMapper;
import com.ygkj.water.project.utils.WaveHeightUtils;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Log4j2
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = ProjectManageApplication.class)
public class WindSpeedTask {

    @Autowired
    private WindSpeedMapper windSpeedMapper;

    @Autowired
    private SectionConfigurationMapper sectionConfigurationMapper;

    @Autowired
    private SectionStormMapper sectionStormMapper;

//    /**
//     * 同步风速
//     */
//    @Scheduled(cron = "0 0/30 * * * ?")
//    @Async(value = "myExecutor")
////    @Test
//    public void synWindSpeed() {
//        log.info("开始同步风速");
//        String url = "http://www.wz121.com/chartweb/changeData";
//        Map<String, String> map = new HashMap<>();
//        map.put("state", "city");
//        map.put("stationId", "58659");
//        String httpPost = null;
//        List<WindSpeedRecord> arrayList = new ArrayList<>();
//        try {
//            httpPost = HttpClientUtil.httpPost(url, map, null);
//            JSONObject jsonObject = JSON.parseObject(httpPost);
//            List<JSONObject> list = JSON.parseArray(jsonObject.getString("future24HourList"), JSONObject.class);
//            String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//            int hour = LocalDateTime.now().getHour();
//            for (int i = 0; i < list.size(); i++) {
//                JSONObject object = list.get(i);
//                Integer xTime = object.getInteger("xTime");
//                double windSpeed = object.getDoubleValue("windSpeed");
//                WindSpeedRecord windSpeedRecord = new WindSpeedRecord();
//                String hours = xTime + ":00:00";
//                if (xTime < 10) {
//                    hours = "0" + xTime + ":00:00";
//                }
//                if (xTime >= hour) {
//                    windSpeedRecord.setTime(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", now + " " + hours));
//                } else {
//                    windSpeedRecord.setTime(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss", tomorrow + " " + hours));
//                }
//                windSpeedRecord.setWindSpeed(windSpeed > 0 ? windSpeed : 0.1d);
//                arrayList.add(windSpeedRecord);
//            }
//            windSpeedMapper.replaceAll(arrayList);
//
//            calculate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 同步风速
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    @Async(value = "myExecutor")
//    @Test
    public void synWindSpeed() {
        log.info("开始同步风速");
        String url = "https://mp.wztf121.com/data/wzweather/v2/townhour/330302.json?r=" + System.currentTimeMillis();
        String httpGet = null;
        List<WindSpeedRecord> arrayList = new ArrayList<>();
        try {
            httpGet = HttpClientUtil.httpGet(url);
            JSONObject jsonObject = JSON.parseObject(httpGet);
            List<JSONObject> list = JSON.parseArray(jsonObject.getString("hourly"), JSONObject.class);
            for (int i = 0; i < list.size(); i++) {
                JSONObject object = list.get(i);
                String xTime = object.getString("time") + ":00";
                double windSpeed = object.getDoubleValue("wind_spend");
                WindSpeedRecord windSpeedRecord = new WindSpeedRecord();
                windSpeedRecord.setTime(DateUtils.parseDate("yyyy-MM-dd HH:mm:ss",xTime));
                windSpeedRecord.setWindSpeed(windSpeed > 0 ? windSpeed : 0.1d);
                arrayList.add(windSpeedRecord);
            }
            windSpeedMapper.replaceAll(arrayList);

            calculate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算24小时H1,H2,H13,潮位
     */
//    @Scheduled(cron = "0 5 0/3 * * ?")
//    @Async
    public void calculate() {
        log.info("开始计算风暴潮潮位");
        List<SectionStorm> sectionStormList = new ArrayList<>();
        //查询所有断面数据
        List<SectionConfiguration> list = sectionConfigurationMapper.selectList(null);
        LambdaQueryWrapper<WindSpeedRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(WindSpeedRecord::getTime, new Date());
        //查询未来的风速
        List<WindSpeedRecord> speedRecordList = windSpeedMapper.selectList(queryWrapper);
        Date minDate = new Date();
        Date maxDate = minDate;
        for (WindSpeedRecord windSpeedRecord : speedRecordList) {
            Date time = windSpeedRecord.getTime();
            if (time.before(minDate)) {
                minDate = time;
            }
            if (time.after(maxDate)) {
                maxDate = time;
            }
        }

        //查询未来的天文潮
        List<StAstrotdR> astrotdRList = windSpeedMapper.listAstrotd(minDate, maxDate);
        Map<Date, Double> astrotdMap = CollectionUtils.isEmpty(astrotdRList) ? Collections.emptyMap() :
                astrotdRList.stream().collect(Collectors.toMap(StAstrotdR::getYmdh, StAstrotdR::getTdz));
        for (SectionConfiguration sectionConfiguration : list) {
            Double fetchLength = sectionConfiguration.getFetchLength();
            Double averageDepth = sectionConfiguration.getAverageDepth();
            String sectionConfigurationId = sectionConfiguration.getId();
            for (WindSpeedRecord windSpeedRecord : speedRecordList) {
                Date time = windSpeedRecord.getTime();
                if (astrotdMap.containsKey(time)) {
                    Double tdz = astrotdMap.get(time);
                    SectionStorm sectionStorm = new SectionStorm();
                    sectionStorm.setTm(windSpeedRecord.getTime());
                    WaveHeight waveHeight = WaveHeightUtils.WaveHeight(windSpeedRecord.getWindSpeed(), 10, fetchLength, averageDepth);
                    sectionStorm.setSectionId(sectionConfigurationId);
                    sectionStorm.setH1(add(tdz,waveHeight.getH1()));
                    sectionStorm.setH2(add(tdz,waveHeight.getH2()));
                    sectionStorm.setH13(add(tdz,waveHeight.getH13()));
                    sectionStormList.add(sectionStorm);
                }
            }
        }
        sectionStormMapper.replaceAll(sectionStormList);
    }

    private Double add(Double tdz, double h1) {
        Double d = tdz + h1;
        if(d == null || d.isNaN()){
            d = 0d;
        }
        return d;
    }
}
