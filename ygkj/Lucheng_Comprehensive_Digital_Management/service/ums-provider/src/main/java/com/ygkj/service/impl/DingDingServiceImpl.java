package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dcxx.framework.watermanagesdk.utils.EncryptHelper;
import com.ygkj.enums.WaterManageEnum;
import com.ygkj.service.DingDingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("dingDingService")
public class DingDingServiceImpl implements DingDingService {

    @Value("${watermanage.tokenkey}")
    private String KEY_TOKEN;
    @Value("${watermanage.appkey}")
    private String APP_KEY;
    @Value("${watermanage.appsecret}")
    private String APP_SECRET;
    @Value("${watermanage.data_tokenkey}")
    private String DATA_KEY_TOKEN;
    @Value("${watermanage.data_appkey}")
    private String DATA_APP_KEY;
    @Value("${watermanage.data_appsecret}")
    private String DATA_APP_SECRET;
    private static final Long KEY_EXPIRE = 60 * 60L;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RestTemplate restTemplate;

    /**
     * 向水管理平台发送带参数的post请求
     *
     * @param suffix
     * @param value
     * @return
     */
    @Override
    public JSONObject post(WaterManageEnum suffix, Object value, String type) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        String token = getToken(type);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将token放到请求头里
        headers.set("watermanage_access_token", token);
        Map<String, Object> paramMap = null;
        //设置请求参数
        if (value != null) {
            paramMap = new HashMap<>(2);
            paramMap.put(suffix.getParamKey(), value);
        }

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(WaterManageEnum.BASE.getUrl() + suffix.getUrl(), httpEntity, JSONObject.class);
        return responseEntity.getBody();
    }

    /**
     * 向水管理平台发送不带参数的post请求
     *
     * @param suffix
     * @return
     */
    @Override
    public JSONObject post(WaterManageEnum suffix) {
        return post(suffix, null, null);
    }

    /**
     * 获取水管理平台的token
     *
     * @return
     */
    @Override
    public String getToken(String type) {
        String keyToken = "";
        String appKey = "";
        String appSecret = "";
        if (type == null) {
            type = "1";
        }
        switch (type) {
            case "2"://数据中心
                keyToken = DATA_KEY_TOKEN;
                appKey = DATA_APP_KEY;
                appSecret = DATA_APP_SECRET;
                break;
            case "1":
            default://水域占用审批
                keyToken = KEY_TOKEN;
                appKey = APP_KEY;
                appSecret = APP_SECRET;
                break;
        }
        if (redisTemplate.hasKey(keyToken)) {
            return (String) redisTemplate.opsForValue().get(keyToken);
        }

        Map<String, Object> signMap = new HashMap<>(8);
        signMap.put("appkey", appKey);
        signMap.put("appsecret", appSecret);
        signMap.put("nonce", "abcd3216");
        long timestamp = System.currentTimeMillis();
        signMap.put("timestamp", timestamp);
        String sign = "";
        try {
            sign = generateSign(signMap);
        } catch (Exception e) {
            log.error("[生成数据签名出错]", e);
            return "";
        }

        JSONObject params = new JSONObject();
        params.put("nonce", "abcd3216");
        params.put("appkey", appKey);
        params.put("timestamp", timestamp);
        params.put("sign", sign);
        String result = restTemplate.postForObject(WaterManageEnum.BASE.getUrl() + WaterManageEnum.TOKEN.getUrl(), params, String.class);
        JSONObject json = JSONObject.parseObject(result);
        String token = json.getJSONObject("message").getString("access_token");
        redisTemplate.opsForValue().set(keyToken, token, KEY_EXPIRE, TimeUnit.SECONDS);
        return token;
    }

    /**
     * @param signMap 签名参数
     * @return
     * @Description 生成数据签名
     **/
    private String generateSign(Map<String, Object> signMap) throws Exception {
        //对key进行排序
        Set<String> set = signMap.keySet();
        Object[] keyArray = set.toArray();
        Arrays.sort(keyArray);

        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Object key : keyArray) {
            // a=aa&bb=bb&c=cc
            if (first) {
                first = false;
            } else {
                builder.append("&");
            }
            builder.append(key.toString().toLowerCase() + "=" + signMap.get(key));
        }

        String context = builder.toString();
        //hash摘要算法
        return EncryptHelper.sha(context);
    }

    /**
     * aes解密
     *
     * @param user
     * @param key
     */
    @Override
    public String aesDecrypt(JSONObject user, String key, String type) {
        try {
            String appSecret = "";
            switch (type) {
                case "2"://数据中心
                    appSecret = DATA_APP_SECRET;
                    break;
                case "1":
                default://水域占用审批
                    appSecret = APP_SECRET;
                    break;
            }
            String encrypt = user.getString(key);
            if (!StringUtils.isBlank(encrypt)) {
                return EncryptHelper.aesDecrypt(encrypt, appSecret);
            }
        } catch (Exception e) {
            log.info("[aes解密出错]", e);
        }
        return "";
    }
}
