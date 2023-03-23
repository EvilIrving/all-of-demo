package com.ygkj.utils.loginUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.xxpt.gateway.shared.client.http.PostClient;
import com.ygkj.constants.ZlbConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Component
@Slf4j
public class ZlbLoginUtils {


    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    private RestTemplate restTemplate;

    @PostConstruct
    void init() {
        restTemplate = restTemplateBuilder.build();
    }


    public String getTokenByTicketId(String ticketId) {

        HttpHeaders headers = getHttpHeaders(ZlbConstants.ACCESS_TOKEN_URL);
        JSONObject body = new JSONObject();
        body.put("appId", ZlbConstants.APP_ID);
        body.put("ticketId", ticketId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(ZlbConstants.ACCESS_TOKEN_URL, request, String.class);
        return checkResponse(stringResponseEntity).getJSONObject("data").getString("accessToken");
    }


    public JSONObject getUserInfoByToken(String accessToken) {
        HttpHeaders headers = getHttpHeaders(ZlbConstants.GET_USER_INFO_URL);
        JSONObject body = new JSONObject();
        body.put("token", accessToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(ZlbConstants.GET_USER_INFO_URL, request, String.class);
        return checkResponse(stringResponseEntity).getJSONObject("data");
    }

    private JSONObject checkResponse(ResponseEntity<String> stringResponseEntity) {
        log.info(stringResponseEntity.getBody());
        if (!stringResponseEntity.getStatusCode().is2xxSuccessful()) {
            //请求失败
            throw new RuntimeException("status:" + stringResponseEntity.getStatusCodeValue() + " " + stringResponseEntity.getBody());
        }
        JSONObject result = JSON.parseObject(stringResponseEntity.getBody());
        if (result.containsKey("errorCode") && result.getString("errorCode") != null && !result.getBooleanValue("success")) {
            //业务错误
            throw new RuntimeException(result.toString());
        }
        return result;
    }

    public String ZwwGetTokenByTicketId(String ticketId) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(date);
        String sign = Md5Utils.irsMd5(ZlbConstants.IRS_AK + ZlbConstants.IRS_SK + time);

        HttpHeaders headers = ZwwGetHttpHeaders(ZlbConstants.ZWW_ACCESS_TOKEN_URL);
        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("servicecode", ZlbConstants.IRS_AK);
        body.add("method", "ticketValidation");
        body.add("time", time);
        body.add("sign", sign);
        body.add("st", ticketId);
        body.add("datatype", "json");


        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(ZlbConstants.ZWW_ACCESS_TOKEN_URL, request, String.class);
        return checkResponse(stringResponseEntity).getString("token");
    }

    public JSONObject ZwwGetUserInfoByToken(String accessToken) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(date);
        String sign = Md5Utils.irsMd5(ZlbConstants.IRS_AK + ZlbConstants.IRS_SK + time);

        HttpHeaders headers = ZwwGetHttpHeaders(ZlbConstants.ZWW_GET_USER_INFO_URL);
        MultiValueMap body = new LinkedMultiValueMap<>();
        body.add("servicecode", ZlbConstants.IRS_AK);
        body.add("method", "getUserInfo");
        body.add("time", time);
        body.add("sign", sign);
        body.add("token", accessToken);
        body.add("datatype", "json");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(ZlbConstants.ZWW_GET_USER_INFO_URL, request, String.class);
        return checkResponse(stringResponseEntity);
    }

    private HttpHeaders getHttpHeaders(String url) {
        IrsSignRes res = IrsUtils.sign(url, "POST");

        HttpHeaders headers = new HttpHeaders();
        headers.add(ZlbConstants.X_BG_HMAC_ACCESS_KEY, res.getAccessKey());
        headers.add(ZlbConstants.X_BG_HMAC_ALGORITHM, res.getAlgorithm());
        headers.add(ZlbConstants.X_BG_HMAC_SIGNATURE, res.getSignature());
        headers.add(ZlbConstants.X_BG_DATE_TIME, res.getDateTime());
        return headers;
    }

    private HttpHeaders ZwwGetHttpHeaders(String url) {
        IrsSignRes res = IrsUtils.sign(url, "POST");

        HttpHeaders headers = new HttpHeaders();
        headers.add(ZlbConstants.X_BG_HMAC_ACCESS_KEY, res.getAccessKey());
        headers.add(ZlbConstants.X_BG_HMAC_ALGORITHM, res.getAlgorithm());
        headers.add(ZlbConstants.X_BG_HMAC_SIGNATURE, res.getSignature());
        headers.add(ZlbConstants.X_BG_DATE_TIME, res.getDateTime());
        headers.add(ZlbConstants.CONTENT_TYPE, ZlbConstants.X_WWW_FORM_URLENCODED);
        return headers;
    }

    public static void main(String[] args) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(date);
        String sign = Md5Utils.irsMd5(ZlbConstants.IRS_AK + ZlbConstants.IRS_SK + time);
        System.out.println(time);
        System.out.println(sign);
    }
}
