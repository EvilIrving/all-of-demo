package com.ygkj.lcsyn.utils;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.enums.WzApiEnum;
import com.ygkj.lcsyn.model.TokenEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component("tokenUtil")
public class TokenUtil {

    @Resource
    private RestTemplate restTemplate;

    private Map<String, TokenEntity> tokenMap = new HashMap<>();

    public String getToken(WzApiEnum apiEnum){
        if (tokenMap.containsKey(apiEnum.getTokenKey()) && tokenMap.get(apiEnum.getTokenKey()).getLocalDateTime().isAfter(LocalDateTime.now())) {
            return tokenMap.get(apiEnum.getTokenKey()).getToken();
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> map = new HashMap();
            map.put("apiKey", apiEnum.getKey());
            map.put("apiSecret", apiEnum.getSecret());
            HttpEntity request = new HttpEntity(map, headers);
            String rs = restTemplate.postForObject("https://sgpt.wzsl.com:8081/api/data-management/busApiApply/getToken", request, String.class);
            JSONObject jsonObject = JSONObject.parseObject(rs);
            String token = jsonObject.getString("data");
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setToken(token);
            tokenEntity.setLocalDateTime(LocalDateTime.now().plusHours(1).plusMinutes(30));
            tokenMap.put(apiEnum.getTokenKey(), tokenEntity);
            return token;
        }
    }

    /**
     * 获取大数据局 token
     * @return
     */
    public static String getDshareToken(){
        String sendUrl= "https://api-dshare.wenzhou.gov.cn/webapi2/get_token";
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("username", "jhq.slqy");
        parameters.put("password", "b263539a-c5cf-11ec-a019-00163e010604");
        JSONObject result= HttpClientUtil.doPost(sendUrl,parameters);
        String token = result.getString("token");
        System.out.println(token);
        String str_token = "jwt "+token;
        return str_token;
    }




}
