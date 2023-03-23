package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.AttendanceApiEnum;
import com.ygkj.lcsyn.mapper.AttendanceMapper;
import com.ygkj.lcsyn.model.AttendanceDataBase;
import com.ygkj.lcsyn.model.AttendancePersonnelBase;
import com.ygkj.lcsyn.utils.AttendanceTokenUtil;
import com.ygkj.lcsyn.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxl
 * @create 2022-07-05 10:31
 * @description  考勤数据同步
 */
@Slf4j
@Component
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = LcSynApplication.class)
public class AttendanceDataTask {

    @Resource
    AttendanceTokenUtil attendanceTokenUtil;

    @Resource
    private AttendanceMapper attendanceMapper;


//    @Test
    @Scheduled(cron = "0 0 7 * * ?")
    public void attendancePersonSyn(){

        String machineToken = attendanceTokenUtil.getMachineToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",machineToken);

        String result = HttpClientUtil.sendGet(AttendanceApiEnum.ATTENDANCE_PERSON.getUrl(), headers, new HashMap<>(), "UTF-8");
        if (StringUtils.isNotBlank(result)){
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getInteger("code") == 1){
                JSONArray data = jsonObject.getJSONArray("data");
                List<AttendancePersonnelBase> personnelBases = data.toJavaList(AttendancePersonnelBase.class);
                attendanceMapper.batchReplacePersonnel(personnelBases);
            }
        }
    }


    @Test
    @Scheduled(cron = "0 0/30 * * * ?")
    public void attendanceDataSyn(){

        String loginToken = attendanceTokenUtil.getLoginToken();
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization",loginToken);
        List<AttendanceDataBase> dataBases = new ArrayList<>();
        String newDateTime = attendanceMapper.findNewDateTime();
        Map<String,Object> params = new HashMap<>();
        int i = 1;
        params.put("pageSize", 20);
        params.put("pageNum", i);
        params.put("startTime",newDateTime);

        String result = HttpClientUtil.sendGet(AttendanceApiEnum.ATTENDANCE_DATA.getUrl(), headers, params, "UTF-8");
        if (StringUtils.isNotBlank(result)){
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getInteger("code") == 1){
                JSONObject data = jsonObject.getJSONObject("data");
                Integer pages = data.getInteger("pages");
                for (int j = 1; j <= pages ; j++) {
                    getRecords(headers, j , dataBases ,newDateTime);
                }
            }
        }
        int flag = 0;
        if (CollectionUtils.isNotEmpty(dataBases)){
            flag = attendanceMapper.batchReplaceDatas(dataBases);
        }
        log.info("本次更新签到记录 {} 条", flag);
    }



    public List<AttendanceDataBase> getRecords(Map<String,String> headers,int j , List<AttendanceDataBase> dataBases , String newDateTime){

        Map<String,Object> params = new HashMap<>();
        params.put("pageSize", 20);
        params.put("pageNum", j);
        params.put("startTime",newDateTime);

        String result = HttpClientUtil.sendGet(AttendanceApiEnum.ATTENDANCE_DATA.getUrl(), headers, params, "UTF-8");
        if (StringUtils.isNotBlank(result)){
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getInteger("code") == 1){
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray records = data.getJSONArray("records");
                List<AttendanceDataBase> bases = records.toJavaList(AttendanceDataBase.class);
                dataBases.addAll(bases);
            }
        }
        return dataBases;
    }



}
