package com.ygkj.lcsyn.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.config.DingDingConfig;
import com.ygkj.lcsyn.enums.ProvinceDataEnum;
import com.ygkj.lcsyn.utils.CommonUtil;
import com.ygkj.lcsyn.utils.EncryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("dingService")
@Slf4j
public class DingService {

    private static final String APP_KEY = "appkey";
    private static final String APP_SECRET = "appsecret";
    private static final String NONCE = "nonce";
    private static final String TIME_STAMP = "timestamp";
    private static final String SIGN = "sign";

    @Value("${watermanage.tokenkey}")
    private String KEY_TOKEN;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 根据手机号给浙政钉发消息
     *
     * @param phone
     * @param context
     */
    public JSONObject sendByPhone(String phone, String context) throws Exception {
        Map<String, Object> param = new HashMap<>();
        phone = EncryUtil.aesEncrypt(phone, DingDingConfig.APP_SECRET);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = CommonUtil.getRandomString(8);
        param.put(APP_KEY, DingDingConfig.APP_KEY);
        param.put(NONCE, nonce);
        param.put(TIME_STAMP, timestamp);
        Map<String,Object> signMap = new HashMap<>(4);
        signMap.put(APP_KEY,DingDingConfig.APP_KEY);
        signMap.put(APP_SECRET,DingDingConfig.APP_SECRET);
        signMap.put(NONCE,nonce);
        signMap.put(TIME_STAMP,timestamp);
        String sign = EncryUtil.sha("appkey=" + DingDingConfig.APP_KEY + "&appsecret=" + DingDingConfig.APP_SECRET + "&nonce=" + nonce + "&timestamp=" + timestamp);
        param.put(SIGN, sign);
        param.put("mobileList", phone);
        param.put("content", context);
        param.put("appSymbol",DingDingConfig.APP_SYMBOL);
        return null;
    }


    /**
     * 向水管理平台发送不带参数的post请求
     *
     * @param provinceDataEnum
     * @param paramMap
     * @return
     */
    public JSONObject post(ProvinceDataEnum provinceDataEnum, Map<String, Object> paramMap) {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将token放到请求头里
        headers.set("watermanage_access_token", token);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(provinceDataEnum.getUrl(), httpEntity, JSONObject.class);
        JSONObject body = responseEntity.getBody();
        return body;
    }

    /**
     * 向水管理平台发送不带参数的post请求
     *
     * @param provinceDataEnum
     * @param paramMap
     * @return
     */
    public JSONObject get(ProvinceDataEnum provinceDataEnum, Map<String, Object> paramMap) {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将token放到请求头里
        headers.set("watermanage_access_token", token);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(provinceDataEnum.getUrl(), HttpMethod.GET, httpEntity, JSONObject.class);
        JSONObject body = responseEntity.getBody();
        return body;
    }

    public String getToken() {
        if (redisTemplate.hasKey(KEY_TOKEN)) {
            return (String) redisTemplate.opsForValue().get(KEY_TOKEN);
        }

        Map<String, Object> signMap = new HashMap<>(8);
        signMap.put("appkey", DingDingConfig.APP_KEY);
        signMap.put("appsecret", DingDingConfig.APP_SECRET);
        signMap.put("nonce", "abcd3216");
        long timestamp = System.currentTimeMillis();
        signMap.put("timestamp", timestamp);
        String sign = "";
        try {
            sign = EncryUtil.generateSign(signMap);
        } catch (Exception e) {
            log.error("[生成数据签名出错]", e);
            return "";
        }

        JSONObject params = new JSONObject();
        params.put("nonce", "abcd3216");
        params.put("appkey", DingDingConfig.APP_KEY);
        params.put("timestamp", timestamp);
        params.put("sign", sign);
        String result = restTemplate.postForObject(ProvinceDataEnum.TOKEN.getUrl(), params, String.class);
        JSONObject json = JSONObject.parseObject(result);
        String token = json.getJSONObject("message").getString("access_token");
        redisTemplate.opsForValue().set(KEY_TOKEN, token, 90, TimeUnit.MINUTES);
        return token;
    }
}
