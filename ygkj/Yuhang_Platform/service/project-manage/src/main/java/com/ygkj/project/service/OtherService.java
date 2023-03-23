package com.ygkj.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.util.HttpUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtherService {

    /**
     * 获取最新天气
     * @param areaCode
     * @return
     */
    public JSONObject getWeather(String areaCode) {
        //获取网页中的xml
        String url = "http://d1.weather.com.cn/sk_2d/"+areaCode+".html?_=" + System.currentTimeMillis();
        Map<String,String> headers = new HashMap<>();
        headers.put("Referer","http://www.weather.com.cn/");
        headers.put("Host","d1.weather.com.cn");
        headers.put("Cookie","f_city=%E6%9D%AD%E5%B7%9E%7C101210101%7C; Hm_lvt_080dabacb001ad3dc8b9b9049b36d43b=1651029077; Hm_lpvt_080dabacb001ad3dc8b9b9049b36d43b=1651039677");
        String resultStr = HttpUtils.sendGet(url, headers, null, "UTF-8");
        JSONObject urlJo = JSONObject.parseObject(resultStr.substring(11));

        JSONObject retJo = new JSONObject();
        retJo.put("temperature",urlJo.getString("temp"));// 温度
        retJo.put("windPower",urlJo.getString("WD") + " " + urlJo.getString("WS"));// 风力方向
        String weather = urlJo.getString("weather");
        retJo.put("weather",weather);// 天气情况
        if (weather.contains("雨")) {
            retJo.put("dayPictureUrl", "yu.png");
        } else if (weather.contains("阴")) {
            retJo.put("dayPictureUrl", "yin.png");
        } else if (weather.contains("晴")) {
            retJo.put("dayPictureUrl", "qing.png");
        } else if (weather.contains("云")) {
            retJo.put("dayPictureUrl", "duoyun.png");
        }
        return retJo;
    }

    //当前月份加一
    public static String addMonth(Date date) {
        if (null == date) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);   //设置当前日期
        c.add(Calendar.MONTH, 1); //月份加1天
        date = c.getTime();
        SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM");
        return dateFm.format(date);
    }

}
