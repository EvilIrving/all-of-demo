package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.YgSynApiEnum;
import com.ygkj.lcsyn.mapper.BusinessMapper;
import com.ygkj.lcsyn.model.PushPpData;
import com.ygkj.lcsyn.model.PushRsvrData;
import com.ygkj.lcsyn.utils.CommonUtil;
import com.ygkj.lcsyn.utils.SynTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lxl
 * @create 2022-07-15 11:16
 * @description 全量数据 推送数据到禹贡平台
 */
@Slf4j
@Component
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = LcSynApplication.class)
public class PushRainAndWaterTask {

    @Resource
    private BusinessMapper businessMapper;

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private SynTokenUtil synTokenUtil;


    @Test
    public void pushRainData(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022,6,1,0,0,0);

        for (calendar.set(2022,0,1,0,0,0); calendar.before(Calendar.getInstance()) ; calendar.add(Calendar.MONTH,1)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            System.out.println(sdf.format(calendar.getTime()));
            String format = sdf.format(calendar.getTime());
            String tableName = CommonUtil.getTableName("PP", calendar.getTime());
            List<PushPpData> ppDate = businessMapper.findPpDate(tableName, format);
            pushPpData(ppDate);
        }
    }


    @Test
    public void pushWaterData(){

        Calendar calendar = Calendar.getInstance();
        calendar.set(2022,0,1,0,0,0);

        for (calendar.set(2022,0,1,0,0,0); calendar.before(Calendar.getInstance()) ; calendar.add(Calendar.MONTH,1)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
            System.out.println(sdf.format(calendar.getTime()));
            String tableName = CommonUtil.getTableName("RSVR", calendar.getTime());
            List<PushRsvrData> ppDate = businessMapper.findWaterDate(tableName);
            pushRsvrData(ppDate);
        }
    }



    /**
     * 推送雨量数据
     * @param ppDate
     */
    public void pushPpData(List<PushPpData> ppDate){

        if (CollectionUtils.isNotEmpty(ppDate)){
            String token = synTokenUtil.getLoginToken();
            Integer total = ppDate.size();
            //每次最大插入500条
            int fromIndex = 0;
            int toIndex;
            do {
                toIndex = fromIndex + 500;
                List<PushPpData> subList = ppDate.subList(fromIndex, toIndex > total ? total : toIndex);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.set("authorization", token);
                String jsonStr = JSON.toJSONStringWithDateFormat(subList,"yyyy-MM-dd HH:mm:ss");
                MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
                params.add("jsonData",jsonStr);

                HttpEntity request = new HttpEntity(params, headers);
//                HttpMessageConverter httpMessageConverter = new FormHttpMessageConverter();
                String result = restTemplate.postForObject(YgSynApiEnum.SYN_PP.getUrl(), request , String.class);
                if (StringUtils.isNotBlank(result)){
                    JSONObject jsonRes = JSONObject.parseObject(result);
                    if (StringUtils.isNotBlank(jsonRes.getString("status")) && "1".equals(jsonRes.getString("status"))){
                        log.info("操作成功:" + toIndex);
                    }
                }
                fromIndex = toIndex;
            } while (toIndex < total);
        }
    }



    public void pushRsvrData(List<PushRsvrData> ppDate){

        if (CollectionUtils.isNotEmpty(ppDate)){
            String token = synTokenUtil.getLoginToken();
            Integer total = ppDate.size();
            //每次最大插入500条
            int fromIndex = 0;
            int toIndex;
            do {
                toIndex = fromIndex + 500;
                List<PushRsvrData> subList = ppDate.subList(fromIndex, toIndex > total ? total : toIndex);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.set("authorization", token);
                String jsonStr = JSON.toJSONStringWithDateFormat(subList,"yyyy-MM-dd HH:mm:ss");
                MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
                params.add("jsonData",jsonStr);
                HttpEntity request = new HttpEntity(params, headers);
                String result = restTemplate.postForObject(YgSynApiEnum.SYN_RSVR.getUrl(), request , String.class);
                if (StringUtils.isNotBlank(result)){
                    log.info(result);
                }
                fromIndex = toIndex;
            } while (toIndex < total);
        }
    }



    @Test
    public void insertDateSyn(){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date pp = sdf.parse("2022-07-15 12:00:00");
            Date rsvr = sdf.parse("2022-07-15 11:15:02");
            pushNearlyPpData(pp);
            pushNearlyRsvrData(rsvr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    /**
     * 推送最近雨量数据
     * @param nearlyDate
     */
    public void pushNearlyPpData(Date nearlyDate){

        String tableName = CommonUtil.getTableName("PP", nearlyDate);
        //根据最近时间进行推送
        List<PushPpData> ppData = businessMapper.findPpDateByTime(tableName, nearlyDate, new Date());
        pushPpData(ppData);
    }



    public void pushNearlyRsvrData(Date nearlyDate){
        String tableName = CommonUtil.getTableName("RSVR", nearlyDate);

        //根据最近时间进行推送
        List<PushRsvrData> rsvrData = businessMapper.findRsvrDateByTime(tableName, nearlyDate, new Date());
        pushRsvrData(rsvrData);
    }




    @Test
    public void testInsert(){
        String token = synTokenUtil.getLoginToken();
        log.info("token = {}",token);
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON); 2022-01-01 00:00:00
        headers.set("authorization", token);
        Map<String, String> map = new HashMap();
        map.put("jsonData", "[{\"stationCode\":\"330301201\",\"timeD\":2022-01-01 00:00:00,\"precipitationTime\":0.0,\"dailyPrecipitation\":0}]");
        HttpEntity request = new HttpEntity(map, headers);
        String result = restTemplate.postForObject(YgSynApiEnum.SYN_PP.getUrl(), request , String.class);
        log.info(result);
    }



}
