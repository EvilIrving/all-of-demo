package com.ygkj.lcsyn.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.AttendanceApiEnum;
import com.ygkj.lcsyn.model.TokenEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lxl
 * @create 2022-07-05 9:23
 * @description 考勤系统token获取
 */
@Component("attendanceTokenUtil")
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = LcSynApplication.class)
public class AttendanceTokenUtil{

    //考勤平台账号密码
    private static String USERNAME = "zjyg";
    private static String PASSWORD = "a828kfzT5kbH";

    private static String MAC_ID = "e6a88c034578";

    private Map<String, TokenEntity> tokenMap = new HashMap<>();

    @Resource
    private RestTemplate restTemplate;


//    @Test
//    public void main() {
//        String loginToken = getMachineToken();
//        System.out.println(loginToken);
//    }


    public String getLoginToken(){
        if (tokenMap.containsKey(AttendanceApiEnum.ATTENDANCE_LOGIN.getUrl()) &&
                tokenMap.get(AttendanceApiEnum.ATTENDANCE_LOGIN.getUrl()).getLocalDateTime().isAfter(LocalDateTime.now())) {
            return tokenMap.get(AttendanceApiEnum.ATTENDANCE_LOGIN.getUrl()).getToken();
        } else {
            JSONObject params = new JSONObject();
            params.put("username",USERNAME);
            params.put("password",PASSWORD);
            try {
                String result = restTemplate.postForObject(AttendanceApiEnum.ATTENDANCE_LOGIN.getUrl(), params, String.class);
                if (StringUtils.isNotBlank(result)){
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getInteger("code") == 1){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String token = data.getString("token");
                        TokenEntity tokenEntity = new TokenEntity();
                        tokenEntity.setToken(token);
                        tokenEntity.setLocalDateTime(LocalDateTime.now().plusHours(1).plusMinutes(30));
                        tokenMap.put(AttendanceApiEnum.ATTENDANCE_LOGIN.getUrl(), tokenEntity);
                        return token;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }



    public String getMachineToken(){
        if (tokenMap.containsKey(AttendanceApiEnum.ATTENDANCE_MACHINE_LOGIN.getUrl()) &&
                tokenMap.get(AttendanceApiEnum.ATTENDANCE_MACHINE_LOGIN.getUrl()).getLocalDateTime().isAfter(LocalDateTime.now())) {
            return tokenMap.get(AttendanceApiEnum.ATTENDANCE_MACHINE_LOGIN.getUrl()).getToken();
        } else {
            Map<String,String> params = new HashMap<>(2);
            params.put("macId",MAC_ID);
            try {
                String result = HttpClientUtil.httpPost(AttendanceApiEnum.ATTENDANCE_MACHINE_LOGIN.getUrl(), params, new HashMap<>());
                if (StringUtils.isNotBlank(result)){
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getInteger("code") == 1){
                        String token = jsonObject.getString("data");
                        TokenEntity tokenEntity = new TokenEntity();
                        tokenEntity.setToken(token);
                        tokenEntity.setLocalDateTime(LocalDateTime.now().plusHours(1).plusMinutes(30));
                        tokenMap.put(AttendanceApiEnum.ATTENDANCE_MACHINE_LOGIN.getUrl(), tokenEntity);
                        return token;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }




}
