package com.ygkj.river.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.xxpt.gateway.shared.client.http.ExecutableClient;
import com.alibaba.xxpt.gateway.shared.client.http.PostClient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**
 * @描述 政务钉钉操作工具类
 * @参数
 * @返回值
 * @创建人 wd
 * @创建时间 2020/7/16 15:05
 * @修改人和其它信息 https://openplatform-portal.dg-work.cn/#/doc-jsapi?apiType=serverapi&docKey=2536
 * <p>
 * https://openplatform-portal.dg-work.cn/portal/
 * <p>
 * 政务钉钉域名：openplatform.dg-work.cn
 * 浙政钉域名  ：openplatform-pro.ding.zj.gov.cn
 */
public class ZwDingDingUtil {

    public static final String APP_SECRET = "sBM4C80FVw3I70je0O4ky1w5yBsqg45K07Fw06D2";
    public static final String APP_KEY = "wz-pt-patrol-JcdUZQ34qX4Q7jiea";
    //    public static final String DOMAIN_NAME = "openplatform-pro.ding.zj.gov.cn";
    public static final String DOMAIN_NAME = "openplatform.dg-work.cn";

    /**
     * @description: 鉴权第一步，获取token
     * @author: huang.zh
     * @return: java.lang.String
     * @Date: 2019/7/17 14:03
     * @params:[]
     */
    public static String getToken() throws Exception {
        ExecutableClient executableClient = ExecutableClient.getInstance();
        executableClient.setAccessKey(APP_KEY);
        executableClient.setSecretKey(APP_SECRET);
        executableClient.setDomainName(DOMAIN_NAME);
        executableClient.setProtocal("https");
        executableClient.init();

        String api = "/gettoken.json";
        PostClient postClient = executableClient.newPostClient(api);
        //设置参数
        postClient.addParameter("appkey", APP_KEY);
        postClient.addParameter("appsecret", APP_SECRET);
        //调用API
        String apiResult = postClient.post();
        System.out.println("getToken:" + apiResult);
        String token = JSON.parseObject(apiResult).getJSONObject("content")
                .getJSONObject("data").getString("accessToken");
        return token;
    }

    /**
     * @description:鉴权第二步，根据token生成api调用票据，默认有效期两小时
     * @author: huang.zh
     * @return: java.lang.String
     * @Date: 2019/7/17 14:04
     * @params:[token]
     */
    public static String getTicket(String token) throws Exception {
        ExecutableClient executableClient = ExecutableClient.getInstance();
        executableClient.setAccessKey(APP_KEY);
        executableClient.setSecretKey(APP_SECRET);
        executableClient.setDomainName(DOMAIN_NAME);
        executableClient.setProtocal("https");
        executableClient.init();

        String api = "/get_jsapi_token.json";
        PostClient postClient = executableClient.newPostClient(api);
        //设置参数
        postClient.addParameter("appkey", APP_KEY);
        postClient.addParameter("appsecret", APP_SECRET);
        postClient.addParameter("accessToken", token);
        //调用API
        String apiResult = postClient.post();
        System.out.println("get_jsapi_token:" + apiResult);
        String ticket = JSON.parseObject(apiResult).getJSONObject("content")
                .getJSONObject("data").getString("accessToken");
        return ticket;
    }

    /**
     * @description:生成鉴权签名
     * @author: huang.zh
     * @return: java.lang.String
     * @Date: 2019/7/17 15:12
     * @params:[ticket, nonceStr, timeStamp, url]
     */
    public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws Exception {
        String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp
                + "&url=" + url;
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        sha1.reset();
        sha1.update(plain.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(sha1.digest());
    }


    private static String bytesToHex(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    public static void main(String[] args) {
        try {
            String token = ZwDingDingUtil.getToken();
            System.out.println(token);
            String ticket = getTicket(token);
            System.out.println(ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
