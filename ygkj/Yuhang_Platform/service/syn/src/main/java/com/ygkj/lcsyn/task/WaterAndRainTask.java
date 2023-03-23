package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.mapper.BasicMapper;
import com.ygkj.lcsyn.mapper.BusinessMapper;
import com.ygkj.lcsyn.model.St;
import com.ygkj.lcsyn.utils.SnowFlake;
import com.ygkj.lcsyn.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WaterAndRainTask {

    @Value("${zs.username}")
    private String username;

    @Value("${zs.password}")
    private String password;

    @Resource
    private BusinessMapper businessMapper;

    @Resource
    private BasicMapper basicMapper;

    @Resource
    private RestTemplate restTemplate;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

    /**
     * 同步雨量
     */
    @Async("asyncTaskExecutor")
//    @Scheduled(cron = "0 0/10 * * * ?")
    public void run() {
        log.info("------------------开始同步水雨情-------------------");
        //查询所有测站
        List<St> stList = basicMapper.listSt();
        Map<String, String> stMap = stList.stream().collect(Collectors.toMap(St::getStcd, St::getSttp));
        //鉴权
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        //查询前二十分钟的数据
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> param = new HashMap<>();
        param.put("endTime", dtf.format(now));
        param.put("beginTime", dtf.format(now.minusMinutes(20)));

        //请求外部接口
        HttpEntity httpEntity = new HttpEntity(param, headers);
        JSONObject jsonObject = restTemplate.postForObject("https://cloud.yhswszy.com:8012/dataCenter/api/dataServer/realtimedata/querybytime", httpEntity, JSONObject.class);

        int code = jsonObject.getInteger("code");
        Integer success = 0;
        if (success.equals(code)) {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray realTimeDatas = data.getJSONArray("realTimeDatas");
            if (!CollectionUtils.isEmpty(realTimeDatas)) {
                List<JSONObject> rainfall = new ArrayList<>();
                List<JSONObject> river = new ArrayList<>();
                List<JSONObject> rsvr = new ArrayList<>();
                for (int i = 0; i < realTimeDatas.size(); i++) {
                    JSONObject realTimeData = realTimeDatas.getJSONObject(i);
                    realTimeData.put("id", String.valueOf(SnowFlake.snowFlake.nextId()));
                    String item = realTimeData.getString("item");
                    if ("P5".equals(item)) {
                        //雨量不用做其它判断
                        rainfall.add(realTimeData);
                    } else {
                        //水位根据测站类型入相应的库
                        String stcd = realTimeData.getString("stcd");
                        if (stMap.containsKey(stcd)) {
                            String sttp = stMap.get(stcd);
                            if ("RR".equals(sttp)) {
                                rsvr.add(realTimeData);
                            } else {
                                river.add(realTimeData);
                            }
                        }
                    }
                }
                //批量保存雨量
                if(!CollectionUtils.isEmpty(rainfall)){
                    businessMapper.batchRelaceRainfall(rainfall);
                }

                //批量保存水库水位
                if(!CollectionUtils.isEmpty(rsvr)){
                    businessMapper.batchRelaceRsvr(rsvr);
                }

                //批量保存河道水位
                if(!CollectionUtils.isEmpty(river)){
                    businessMapper.batchRelaceRiver(river);
                }
            }
        }

        log.info("------------------结束同步水雨情-------------------");
    }

    /**
     * 同步雨量
     */
    @Async("asyncTaskExecutor")
    public void run2() {
        log.info("------------------开始同步指标-------------------");
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity httpEntity = new HttpEntity(null, headers);

        //河道站防洪指标
//        updateZb("https://cloud.yhswszy.com:8012/dataCenter/api/dataServer/station/getRVFCCH_B","dts_ra_basic_data_db_st_rvfcch_b",httpEntity);
        //水库防洪指标
//        updateZb("https://cloud.yhswszy.com:8012/dataCenter/api/dataServer/station/getRSVRFCCH_B","dts_yh_basic_data_db_st_rsvrfcch_b",httpEntity);
        //水库汛限水位
//        updateZb("https://cloud.yhswszy.com:8012/dataCenter/api/dataServer/station/getRSVRFSR_B","dts_yh_basic_data_db_st_rsvrfsr_b",httpEntity);
        //水库特征表
//        updateZb("https://cloud.yhswszy.com:8012/dataCenter/api/dataServer/station/getZVARL_B","dts_yh_basic_data_db_st_zvarl_b",httpEntity);

        log.info("------------------结束同步指标-------------------");
    }

    private void updateZb(String url, String tableName,HttpEntity httpEntity) {
        JSONObject jsonObject = restTemplate.postForObject(url, httpEntity, JSONObject.class);
        JSONArray data = jsonObject.getJSONArray("data");

        List<String> sqlList = SqlUtil.batchInsertSql2(data,tableName,600);

        for (String sql:sqlList){
//            System.out.println(sql);
            basicMapper.batchInsert(sql);
        }
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("YhdataHouse", "cc4062642b6aea7b597794cebcf1b542");
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> param = new HashMap<>();
        param.put("beginTime", "2021-12-28 14:40:00");
        param.put("endTime", "2021-12-28 15:00:00");
        HttpEntity httpEntity = new HttpEntity(param, headers);
        JSONObject jsonObject = restTemplate.postForObject("https://cloud.yhswszy.com:8012/dataCenter/api/dataServer/realtimedata/querybytime", httpEntity, JSONObject.class);
        System.out.println(jsonObject);
    }

}

