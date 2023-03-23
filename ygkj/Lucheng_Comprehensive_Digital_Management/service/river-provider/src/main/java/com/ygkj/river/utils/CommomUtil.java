package com.ygkj.river.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.river.vo.response.NearestTheRiverRespVo;
import com.ygkj.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommomUtil {

    /**
     * 百度地图逆地理编码
     */
    public static String reverseGeocodUrl = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=pb7jSAN1f3TXidL6tWIn0ExGi83oCDGW&output=json&coordtype=wgs84ll&" +
            "mcode=A5:0B:E7:27:C2:F5:DB:25:F0:05:3A:A7:5A:5F:32:D9:96:B1:33:82;com.ygkj.ruian";

    /**
     * 地球半径,单位 km
     */
    private static final double EARTH_RADIUS = 6378.137;

    public static void reverseGeocodGps(Double latitude, Double longitude, NearestTheRiverRespVo riverRespVo) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("ak", "pb7jSAN1f3TXidL6tWIn0ExGi83oCDGW");
            params.put("output", "json");
            params.put("coordtype", "wgs84");
            params.put("mcode", "A5:0B:E7:27:C2:F5:DB:25:F0:05:3A:A7:5A:5F:32:D9:96:B1:33:82;com.ygkj.ruian");
            params.put("location", latitude + "," + longitude);
            String resultStr = HttpClientUtil.httpPost(reverseGeocodUrl, params, null);
            JSONObject jo = JSON.parseObject(resultStr);
            Integer status = jo.getInteger("status");
            if (status == 0) {
                JSONObject result = jo.getJSONObject("result");
                String formattedAddress = result.getString("formatted_address");//详细地址
                riverRespVo.setAddress(formattedAddress);
                JSONObject addressComponent = result.getJSONObject("addressComponent");
                String district = addressComponent.getString("district");//区
                riverRespVo.setTown(district);
            }
        } catch (Exception e) {
            log.error("逆地理编码报错信息:{}", e.getMessage());
        }
    }

    /**
     * 根据经纬度，计算两点间的距离
     *
     * @return 返回距离 单位米
     */
    public static double getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }


    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 获取两个字符串格式日期  yyyy-MM-dd 间的相差天数
     * @param date_a
     * @param date_b
     * @return
     */
    public static Long betweenDays(Date date_a, Date date_b) {

        Calendar calendar_a = Calendar.getInstance();// 获取日历对象
        Calendar calendar_b = Calendar.getInstance();
        calendar_a.setTime(date_a);// 设置日历
        calendar_b.setTime(date_b);

        long time_a = calendar_a.getTimeInMillis();
        long time_b = calendar_b.getTimeInMillis();

        long between_days = (time_b - time_a) / (1000 * 3600 * 24);//计算相差天数

        return between_days;
    }

}
