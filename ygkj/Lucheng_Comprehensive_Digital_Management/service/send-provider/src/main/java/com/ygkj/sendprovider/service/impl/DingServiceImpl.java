package com.ygkj.sendprovider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.sendprovider.config.DingDingConfig;
import com.ygkj.sendprovider.service.DingService;
import com.ygkj.sendprovider.util.CommonUtil;
import com.ygkj.sendprovider.util.EncryUtil;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.warning.vo.request.DingWorkReqVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("dingService")
@Slf4j
public class DingServiceImpl implements DingService {

    private static final String APP_KEY = "appkey";
    private static final String APP_SECRET = "appsecret";
    private static final String NONCE = "nonce";
    private static final String TIME_STAMP = "timestamp";
    private static final String MOBILE_LIST = "mobileList";
    private static final String SIGN = "sign";

    @Value("${watermanage.tokenkey}")
    private String KEY_TOKEN;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RestTemplate restTemplate;

    @Autowired
    private GenerateDistributionIDService idService;

    /**
     * 根据手机号给浙政钉发消息
     *
     * @param phone
     * @param context
     */
    @Override
    public JSONObject sendByPhone(String phone, String context) throws Exception {
        Map<String, Object> param = new HashMap<>();
        phone = EncryUtil.aesEncrypt(phone, DingDingConfig.APP_SECRET);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = CommonUtil.getRandomString(8);
        param.put(APP_KEY, DingDingConfig.APP_KEY);
        param.put(NONCE, nonce);
        param.put(TIME_STAMP, timestamp);
        String sign = EncryUtil.sha("appkey=" + DingDingConfig.APP_KEY + "&appsecret=" + DingDingConfig.APP_SECRET + "&nonce=" + nonce + "&timestamp=" + timestamp);
        param.put(SIGN, sign);
        param.put("mobileList", phone);
        param.put("content", context);
        param.put("appSymbol", DingDingConfig.APP_SYMBOL);
        return post(DingDingConfig.URL_SEND_BY_PHONE, param);
    }

    /**
     * 根据手机号获取用户信息
     */
    @Override
    public JSONObject getUserInfoByMobile(String phone) {
        phone = EncryUtil.aesEncrypt(phone, DingDingConfig.APP_SECRET);
        Map<String, Object> param = new HashMap<>();
        param.put("mobile", phone);
        return post(DingDingConfig.URL_USER_BY_PHONE, param);
    }

    /**
     * 新增待办
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject addWork(DingWorkReqVo reqVo) {
        Map<String, Object> param = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        String nonce = CommonUtil.getRandomString(8);
        param.put(NONCE, nonce);
        param.put(TIME_STAMP, timestamp);
//        if(StringUtils.isEmpty(reqVo.getReqId())){
//            reqVo.setReqId(idService.nextID());
//        }
        param.put(TIME_STAMP, timestamp);
        param.put(APP_SECRET, DingDingConfig.APP_SECRET);
        param.putAll(JSONObject.parseObject(JSON.toJSONString(reqVo)));
        param.put(SIGN, EncryUtil.generateSign(param));
        log.info("[发送待办]{}",reqVo);
        return post(DingDingConfig.URL_ADD_WORK,param);
    }

    /**
     * 结束待办
     *
     * @param reqId
     * @return
     */
    @Override
    public JSONObject endWork(String reqId) {
        Map<String, Object> param = getWorkParam(reqId);
        log.info("[完成待办]{}",reqId);
        return post(DingDingConfig.URL_END_WORK,param);
    }

    private Map<String, Object> getWorkParam(String reqId) {
        Map<String, Object> param = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        String nonce = CommonUtil.getRandomString(8);
        param.put(NONCE, nonce);
        param.put("reqId", reqId);
        param.put(TIME_STAMP, timestamp);
        param.put(APP_SECRET, DingDingConfig.APP_SECRET);
        param.put(SIGN, EncryUtil.generateSign(param));
        return param;
    }

    /**
     * 取消待办
     *
     * @param reqId
     * @return
     */
    @Override
    public JSONObject cancelWork(String reqId) {
        Map<String, Object> param = getWorkParam(reqId);
        return post(DingDingConfig.URL_CANCEL_WORK,param);
    }

    /**
     * 处理待办
     *
     * @param reqId
     * @return
     */
    @Override
    public JSONObject dealWork(String reqId) {
        Map<String, Object> param = getWorkParam(reqId);
        return post(DingDingConfig.URL_DEAL_WORK,param);
    }

    /**
     * 发送浙政钉消息
     * @param msgSendRecord
     * @return
     */
    @Override
    public JSONObject sendDingDing(MsgSendRecord msgSendRecord) {
        Map<String, Object> param = new HashMap<>();
        long timestamp = System.currentTimeMillis();
        String nonce = CommonUtil.getRandomString(8);
        param.put(NONCE,nonce);
        param.put(TIME_STAMP,timestamp);
        param.put(APP_KEY,DingDingConfig.APP_KEY);
        param.put(APP_SECRET,DingDingConfig.APP_SECRET);
        param.put(SIGN, EncryUtil.generateSign(param));
        //手机号码需要进行aes加密
        String phone = EncryUtil.aesEncrypt(msgSendRecord.getReceivePhone(),DingDingConfig.APP_SECRET);
        param.put(MOBILE_LIST,phone);
        param.put("content",msgSendRecord.getContext());
        param.put("appSymbol","114");

        return post(DingDingConfig.URL_SEND_DING_MSG,param);
    }

    /**
     * 向浙政钉发送post请求
     *
     * @param url
     * @param paramMap
     * @return
     */
    @Override
    public JSONObject post(String url, Map<String, Object> paramMap) {
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //将token放到请求头里
        headers.set("watermanage_access_token", token);
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url, httpEntity, JSONObject.class);
        return responseEntity.getBody();
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
        String result = restTemplate.postForObject(DingDingConfig.URL_TOKEN, params, String.class);
        JSONObject json = JSONObject.parseObject(result);
        log.info("[获取水管平台token]{}",json);
        String token = json.getJSONObject("message").getString("access_token");
        redisTemplate.opsForValue().set(KEY_TOKEN, token, 90, TimeUnit.MINUTES);
        return token;
    }
}
