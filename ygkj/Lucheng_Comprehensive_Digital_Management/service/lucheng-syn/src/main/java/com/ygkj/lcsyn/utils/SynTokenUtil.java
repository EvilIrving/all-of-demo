package com.ygkj.lcsyn.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.AttendanceApiEnum;
import com.ygkj.lcsyn.enums.YgSynApiEnum;
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
 * @create 2022-07-15 10:37
 * @description
 */
@Component
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = LcSynApplication.class)
public class SynTokenUtil {

    private static String ACCESS_KEY = "yysk";
    private static String ACCESS_SECRET = "vjp9K%ukTAzI35";

    private Map<String, TokenEntity> tokenMap = new HashMap<>();

    @Resource
    private RestTemplate restTemplate;

    @Test
    public void testLogin(){
        String loginToken = getLoginToken();
        System.out.println(loginToken);
    }


    public String getLoginToken(){
        if (tokenMap.containsKey(YgSynApiEnum.SYN_TOKEN.getUrl()) &&
                tokenMap.get(YgSynApiEnum.SYN_TOKEN.getUrl()).getLocalDateTime().isAfter(LocalDateTime.now())) {
            return tokenMap.get(YgSynApiEnum.SYN_TOKEN.getUrl()).getToken();
        } else {
            try {
//                String result = HttpClientUtil.httpGet(YgSynApiEnum.SYN_TOKEN.getUrl() + "?accessKey=" + ACCESS_KEY + "&accessSecret=" + ACCESS_SECRET);
                String result = restTemplate.getForObject(YgSynApiEnum.SYN_TOKEN.getUrl() +"?accessKey="+ACCESS_KEY + "&accessSecret="+ ACCESS_SECRET, String.class );
                if (StringUtils.isNotBlank(result)){
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if (jsonObject.getInteger("status") == 1){
                        String token = jsonObject.getString("data");
                        TokenEntity tokenEntity = new TokenEntity();
                        tokenEntity.setToken(token);
                        tokenEntity.setLocalDateTime(LocalDateTime.now().plusHours(1).plusMinutes(30));
                        tokenMap.put(YgSynApiEnum.SYN_TOKEN.getUrl(), tokenEntity);
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
