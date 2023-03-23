package com.ygkj.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.enums.WaterManageEnum;

public interface DingDingService {

    /**
     * 向水管理平台发送带参数的post请求
     *
     * @param suffix
     * @param value
     * @return
     */
    JSONObject post(WaterManageEnum suffix, Object value,String type);

    /**
     * 向水管理平台发送不带参数的post请求
     *
     * @param suffix
     * @return
     */
    JSONObject post(WaterManageEnum suffix);

    /**
     * 获取水管理平台的token
     *
     * @return
     */
    String getToken(String type);

    /**
     * aes解密
     *
     * @param user
     * @param key
     */
    String aesDecrypt(JSONObject user, String key,String type);
}
