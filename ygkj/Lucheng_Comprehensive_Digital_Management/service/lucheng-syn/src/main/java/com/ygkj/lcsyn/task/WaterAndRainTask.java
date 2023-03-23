package com.ygkj.lcsyn.task;

import ch.qos.logback.classic.db.names.TableName;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.WzApiEnum;
import com.ygkj.lcsyn.mapper.BasicMapper;
import com.ygkj.lcsyn.mapper.BusinessMapper;
import com.ygkj.lcsyn.mapper.SiChuangMapper;
import com.ygkj.lcsyn.model.*;
import com.ygkj.lcsyn.thread.PushThread;
import com.ygkj.lcsyn.utils.CommonUtil;
import com.ygkj.lcsyn.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = LcSynApplication.class)
public class WaterAndRainTask {

    @Value("${wzapi.key}")
    private String apiKey;

    @Value("${wzapi.secret}")
    private String apiSecret;

    @Resource
    private SiChuangMapper siChuangMapper;

    @Resource
    private BusinessMapper businessMapper;

    @Resource
    private BasicMapper basicMapper;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private TokenUtil tokenUtil;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Map<String, TokenEntity> tokenMap = new HashMap<>();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

    //线程池
    ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*2,20,60, TimeUnit.SECONDS
            ,new ArrayBlockingQueue<>(10));

    @Resource
    private PushRainAndWaterTask pushRainAndWaterTask;

    /**
     * 同步雨量
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void synPptn() {
        log.info("------------------开始同步雨量-------------------");
        String tableName = CommonUtil.getTableName("PP",new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,-20);
        Date nearlyDate = calendar.getTime();
        if (StringUtils.isNotBlank(tableName)){
             nearlyDate = businessMapper.findNearly(tableName);
        }
        //从四创查询数据
        List<StPptnR> list = siChuangMapper.listPp();
        if (!CollectionUtils.isEmpty(list)) {
            log.info("[获取雨量数据数量]{}", list.size());
            //数据分类
            Map<String, List<StPptnR>> map = list.stream().collect(Collectors.groupingBy(m -> sdf.format(m.getTm())));
            //批量插入数据
            map.forEach((k, v) -> businessMapper.replacePp("st_pptn_r_" + k, v));
        }
        executor.submit(new PushThread(nearlyDate,pushRainAndWaterTask,"PP"));

        log.info("------------------结束同步雨量-------------------");
    }


    /**
     * 同步河道水位
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void synRiver() {
        log.info("------------------开始同步河道水位------------------");
        //从四创查询数据
        List<StRiverR> list = siChuangMapper.listRiver();
        if (!CollectionUtils.isEmpty(list)) {
            log.info("[获取河道水位数量]{}", list.size());
            //数据分类
            Map<String, List<StRiverR>> map = new HashMap<>();
            Map<String, List<StTideR>> tideMap = new HashMap<>();
            for (StRiverR stRiverR : list) {
                String stcd = stRiverR.getStcd();
                String key = sdf.format(stRiverR.getTm());
                if("06896".equals(stcd)){
                    river2Tide(tideMap,stRiverR,key);
                }
                List<StRiverR> stRiverRList = map.computeIfAbsent(key, k -> new ArrayList<>());
                stRiverRList.add(stRiverR);
            }
            //批量插入数据
            map.forEach((k, v) -> {
                //个别测站还要存入潮位表
                businessMapper.replaceRiver("st_river_r_" + k, v);
            });
            if(!CollectionUtils.isEmpty(tideMap)){
                tideMap.forEach((k, v) -> {
                    businessMapper.replaceTide("st_tide_r_" + k, v);
                });
            }
        }
        log.info("------------------结束同步河道水位------------------");
    }

    private void river2Tide(Map<String, List<StTideR>> tideMap, StRiverR stRiverR,String key) {
        List<StTideR> stTideRList = tideMap.computeIfAbsent(key, k -> new ArrayList<>());
        StTideR stTideR = new StTideR();
        stTideR.setStcd(stRiverR.getStcd());
        stTideR.setTdz(stRiverR.getZ());
        stTideR.setTm(stRiverR.getTm());
        stTideRList.add(stTideR);
    }

    /**
     * 同步水库水位
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void synRsvr() {
        log.info("------------------开始同步水库水位------------------");
        String tableName = CommonUtil.getTableName("RSVR",new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,-20);
        Date nearlyDate = calendar.getTime();
        if (StringUtils.isNotBlank(tableName)){
            nearlyDate = businessMapper.findNearly(tableName);
        }
        //从四创查询数据
        List<StRsvrR> list = siChuangMapper.listRsvr();
        if (!CollectionUtils.isEmpty(list)) {
            log.info("[获取水库水位数量]{}", list.size());
            //数据分类
            Map<String, List<StRsvrR>> map = list.stream().collect(Collectors.groupingBy(m -> sdf.format(m.getTm())));
            //批量插入数据
            map.forEach((k, v) -> {
                businessMapper.replaceRsvr("st_rsvr_r_" + k, v);
            });
        }
        executor.submit(new PushThread(nearlyDate,pushRainAndWaterTask,"RSVR"));
        log.info("------------------结束同步水库水位------------------");

    }

    /**
     * 同步潮位
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 0/10 * * * ?")
    public void synTide() {
        log.info("------------------开始同步潮位------------------");
        //从四创查询数据
        List<StTideR> list = siChuangMapper.listTide();
        if (!CollectionUtils.isEmpty(list)) {
            log.info("[获取潮位数量]{}", list.size());
            //数据分类
            Map<String, List<StTideR>> map = list.stream().collect(Collectors.groupingBy(m -> sdf.format(m.getTm())));
            //批量插入数据
            map.forEach((k, v) -> {
                businessMapper.replaceTide("st_tide_r_" + k, v);
            });
        }
        log.info("------------------结束同步潮位------------------");
    }

//    /**   温州数据接口异常，更改为从温州同步的数据源获取
//     * 同步堰闸水情
//     */
//    @Async("asyncTaskExecutor")
//    @Scheduled(cron = "0 0/10 * * * ?")
//    @Test
    public void synWas() {
        log.info("------------------开始同步堰闸水情------------------");
        //查询所有温瑞平测站
        List<String> list = basicMapper.listWrpSt();
        String stcds = String.join(",", list);

        //获取token
        String token = tokenUtil.getToken(WzApiEnum.WAS);

        //从温州请求数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("authorization", token);
        Map<String, String> map = new HashMap();
        map.put("stcds", stcds);
        LocalDateTime now = LocalDateTime.now();
        map.put("start", now.minusMinutes(20).format(dtf));
        map.put("end", dtf.format(now));
        HttpEntity request = new HttpEntity(map, headers);
        String rs = restTemplate.postForObject(WzApiEnum.WAS.getUrl(), request, String.class);
        JSONObject jsonObject = JSONObject.parseObject(rs);
        String dataS = jsonObject.getString("data");
        JSONObject data = JSONObject.parseObject(dataS);

        JSONArray jsonArray = data.getJSONArray("data");

        //数据分类
        Map<String, List<StWasR>> dataMap = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject wasRes = jsonArray.getJSONObject(i);
            JSONArray waterRains = wasRes.getJSONArray("waterRains");
            if (!CollectionUtils.isEmpty(waterRains)) {
                for (int j = 0; j < waterRains.size(); j++) {
                    JSONObject stWasRJson = waterRains.getJSONObject(j);
                    add2Map(stWasRJson, dataMap);
                }
            }
        }

        //批量保存数据
        if (!dataMap.isEmpty()) {
            dataMap.forEach((k, v) -> {
                businessMapper.replaceWas("st_was_r_" + k, v);
            });
        }
        log.info("------------------结束同步堰闸水情------------------");
    }

    private void add2Map(JSONObject stWasRJson, Map<String, List<StWasR>> dataMap) {
        try {
            StWasR stWasR = JSONObject.parseObject(stWasRJson.toString(), StWasR.class);
            String tm = stWasRJson.getString("tm");
            String key = tm.substring(0, 7).replaceFirst("-", "");
            List<StWasR> list = dataMap.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(stWasR);
        } catch (Exception e) {
            log.error("[堰闸数据异常]{}", stWasRJson, e);
        }
    }

}

