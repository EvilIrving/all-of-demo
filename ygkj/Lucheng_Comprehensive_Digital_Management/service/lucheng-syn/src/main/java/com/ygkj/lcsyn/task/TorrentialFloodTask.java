package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.ProvinceDataEnum;
import com.ygkj.lcsyn.mapper.ProvinceDataMapper;
import com.ygkj.lcsyn.model.*;
import com.ygkj.lcsyn.service.DingService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author lxl
 * @create 2022-05-05 17:18
 * @description
 */
@Slf4j
@Component
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = LcSynApplication.class)
public class TorrentialFloodTask {

    @Resource
    private DingService dingService;

    @Resource
    private ProvinceDataMapper provinceDataMapper;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 重点防御对象信息表
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void synPvDefenObjectsData() throws Exception {

        log.info("同步重点防御对象信息表数据开始");
        ProvinceDataEnum defenseEnum = ProvinceDataEnum.FOCUS_DEFENSE;
        List<PvDefenObject> pvDefenObjects = new ArrayList<>();
        Date defenObjectMaxTongDate = provinceDataMapper.findDefenObjectMaxTongDate();

        List<JSONArray> list = getArrayList(defenseEnum, defenObjectMaxTongDate);

        for (JSONArray jsonArray : list) {
            List<PvDefenObject> pvList = jsonArray.toJavaList(PvDefenObject.class);
            pvDefenObjects.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(pvDefenObjects)){
            i = provinceDataMapper.replaceDefenObject(pvDefenObjects);
        }
        log.info("同步重点防御对象信息表数据结束，更新条数为：{}",i);
    }


    /**
     * 防御对象名录表
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void synPvDefenObjectBaseData() throws Exception {

        log.info("同步防御对象名录表数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.DEFENSE_LIST;
        List<PvDefenObjectBase> pvDefenObjects = new ArrayList<>();

        Date defenObjectBaseMaxTongDate = provinceDataMapper.findDefenObjectBaseMaxTongDate();
        List<JSONArray> list = getArrayList(defenseEnum, defenObjectBaseMaxTongDate);

        for (JSONArray jsonArray : list) {
            List<PvDefenObjectBase> pvList = jsonArray.toJavaList(PvDefenObjectBase.class);
            pvDefenObjects.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(pvDefenObjects)){
            i = provinceDataMapper.replaceDefenObjectBase(pvDefenObjects);
        }
        log.info("同步防御对象名录表数据结束，更新条数为：{}",i);
    }


    /**
     * 山洪预警责任人信息表
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void synPvPchargeInfoData() throws Exception {

        log.info("同步山洪预警责任人信息表数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.FLOOD_WARNING_INFO;
        List<PvPchargeInfo> pvPchargeInfos = new ArrayList<>();

        Date pchargeInfoMaxTongDate = provinceDataMapper.findPchargeInfoMaxTongDate();
        List<JSONArray> list = getArrayList(defenseEnum, pchargeInfoMaxTongDate);

        for (JSONArray jsonArray : list) {
            List<PvPchargeInfo> pvList = jsonArray.toJavaList(PvPchargeInfo.class);
            pvPchargeInfos.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(pvPchargeInfos)){
            i = provinceDataMapper.replacePvPchargeInfo(pvPchargeInfos);
        }
        log.info("同步山洪预警责任人信息表数据结束，更新条数为：{}",i);
    }



    /**
     * 山洪预警责任人名录表
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void synPvPchargeBaseData() throws Exception {

        log.info("同步山洪预警责任人名录表数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.FLOOD_WARNING_REF;
        List<PvPchargeBase> pvPchargeBases = new ArrayList<>();

        Date pchargeBaseMaxTongDate = provinceDataMapper.findPchargeBaseMaxTongDate();
        List<JSONArray> list = getArrayList(defenseEnum, pchargeBaseMaxTongDate);

        for (JSONArray jsonArray : list) {
            List<PvPchargeBase> pvList = jsonArray.toJavaList(PvPchargeBase.class);
            pvPchargeBases.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(pvPchargeBases)){
            i = provinceDataMapper.replacePvPchargeBase(pvPchargeBases);
        }
        log.info("同步山洪预警责任人名录表数据结束，更新条数为：{}",i);
    }




    /**
     * 山洪易发区影响人口信息
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void synPvFloodAffectPopulation() throws Exception {

        log.info("同步山洪易发区影响人口信息表数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.AFFECT_POPULATION;
        List<PvFloodAffectPopulation> populationList = new ArrayList<>();

        Date affectPopulationMaxTongDate = provinceDataMapper.findAffectPopulationMaxTongDate();

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,0,1);

        List<JSONArray> list = getArrayList(defenseEnum, affectPopulationMaxTongDate);

        for (JSONArray jsonArray : list) {
            List<PvFloodAffectPopulation> pvList = jsonArray.toJavaList(PvFloodAffectPopulation.class);
            populationList.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(populationList)){
            i = provinceDataMapper.replacePvAffectPopulation(populationList);
        }
        log.info("同步山洪易发区影响人口信息表结束，更新条数为：{}",i);
    }

    /**
     * 防治对象信息表
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void synPvFloodAffectPopulation2() throws Exception {

        log.info("同步防治对象信息表数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.PREV_OBJ_INFO;
        List<PvPrevObjInfo> populationList = new ArrayList<>();

        Date prevObjInfoMaxTongDate = provinceDataMapper.findPrevObjInfoMaxTongDate();

        List<JSONArray> list = getArrayList(defenseEnum, prevObjInfoMaxTongDate);

        for (JSONArray jsonArray : list) {
            List<PvPrevObjInfo> pvList = jsonArray.toJavaList(PvPrevObjInfo.class);
            populationList.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(populationList)){
            i = provinceDataMapper.replacePvPrevObjInfo(populationList);
        }
        log.info("同步防治对象信息表结束，更新条数为：{}",i);
    }



    /**
     * 防治对象名录表   无有用数据，考虑是否同步
     * @throws Exception
     */
//    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async
    public void synPvFloodAffectPopulatio3n() throws Exception {

        log.info("同步防治对象名录表数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.PREV_OBJ_BASE;
        List<PvPrevObjBase> objBaseList = new ArrayList<>();

        Date prevObjBaseMaxTongDate = provinceDataMapper.findPrevObjBaseMaxTongDate();

        List<JSONArray> list = getArrayList(defenseEnum, prevObjBaseMaxTongDate);

        for (JSONArray jsonArray : list) {
            List<PvPrevObjBase> pvList = jsonArray.toJavaList(PvPrevObjBase.class);
            objBaseList.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(objBaseList)){
            i = provinceDataMapper.replacePvPrevObjBase(objBaseList);
        }
        log.info("同步防治对象名录表结束，更新条数为：{}",i);
    }

    /**
     * 山洪预警
     * @throws Exception
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void synFloodInfo() throws Exception {

        log.info("同步山洪预警数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.FLOOD_WARNING;
        List<PvFloodInfo>  infoList = new ArrayList<>();

        Date floodInfoMaxWarnDateDate = provinceDataMapper.findFloodInfoMaxWarnDate();
        Calendar calendar = Calendar.getInstance();
        calendar.set(DateUtil.yearOf(new Date()),0,1);
        floodInfoMaxWarnDateDate = floodInfoMaxWarnDateDate != null? floodInfoMaxWarnDateDate : calendar.getTime();
        List<JSONArray> list = getArrayList(defenseEnum, floodInfoMaxWarnDateDate);

        for (JSONArray jsonArray : list) {
            List<PvFloodInfo> pvList = jsonArray.toJavaList(PvFloodInfo.class);
            infoList.addAll(pvList);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(infoList)){
            i = provinceDataMapper.replacePvFloodInfo(infoList);
        }
        log.info("同步山洪预警数据结束，更新条数为：{}",i);
    }


    @Scheduled(cron = "0 0 0/10 * * ?")
    @Async
//    @Test
    public void mountainPondWarnResponseSyn() throws Exception {

        log.info("同步山塘预警责任人信息表数据开始");

        ProvinceDataEnum defenseEnum = ProvinceDataEnum.MOUNTAIN_POND_WARN_RESPONSE;
        List<MountainPondWarnResponse> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,0,1);
        List<JSONArray> list = getArrayList(defenseEnum, calendar.getTime());

        for (JSONArray jsonArray : list) {
            List<MountainPondWarnResponse> jsonLists = jsonArray.toJavaList(MountainPondWarnResponse.class);
            result.addAll(jsonLists);
        }
        int i = 0;
        if (CollectionUtils.isNotEmpty(result)){
            i = provinceDataMapper.replaceMountainPondWarn(result);
        }
        log.info("同步山塘预警责任人信息表数据结束，更新条数为：{}",i);

    }



    public ArrayList<JSONArray> jsonArray(ProvinceDataEnum provinceDataEnum, Map<String, Object> map) throws Exception {
        Integer total = Integer.valueOf(String.valueOf(map.get("total")));
        int v = (int) Math.ceil(total * 1.0 / 100);
        ArrayList<JSONArray> jsonArrays = new ArrayList<>();
        map.put("pageSize",100);
        for (int i = 0; i < v; i++) {
            map.put("pageNumber",i + 1);
            JSONObject parseObject = dingService.post(provinceDataEnum, map);
            String status = parseObject.getString("status");
            if ("0".equals(status)) {
                JSONObject message = parseObject.getJSONObject("message");
                JSONArray objects = message.getJSONArray("rows");
                jsonArrays.add(objects);
            }
        }
        return jsonArrays;
    }


    public List<JSONArray> getArrayList(ProvinceDataEnum defenseEnum ,Date maxTongDate) throws Exception {

        List<JSONArray> list = new ArrayList<>();
        //date 转为 LocalDateTime
        Instant instant = maxTongDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime time = instant.atZone(zoneId).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        while (true){
            String start = time.format(dtf);
            time = time.minusDays(-30);
            String end = time.format(dtf);
            Map<String, Object> map = jsonArray(defenseEnum, start, end);
            ArrayList<JSONArray> jsonArrays = jsonArray(defenseEnum, map);
            list.addAll(jsonArrays);
            if (now.isBefore(time)){
                break;
            }
        }
        return list;
    }



    public Map<String, Object> jsonArray(ProvinceDataEnum provinceDataEnum, String time, String format) throws Exception {

        JSONObject object = new JSONObject();
        object.put("pageNumber", 1);
        object.put("pageSize", 10);
        object.put("startTime", time);
        object.put("endTime", format);

        JSONObject parseObject = dingService.post(provinceDataEnum, object);

        String status = parseObject.getString("status");
        if (!"0".equals(status)){
            log.info("{}",parseObject.getString("errmsg"));
            throw new RuntimeException("省数据厅返回未成功");
        }

        JSONObject message = parseObject.getJSONObject("message");
        Integer total = 0;
        if (message != null) {
            total = Integer.valueOf(String.valueOf(message.get("total")));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("startTime", time);
        map.put("endTime", format);
        return map;
    }





}
