package com.ygkj.visualization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.visualization.service.SatelliteCloudChartService;
import org.springframework.stereotype.Service;

@Service("satelliteCloudChartService")
public class SatelliteCloudChartServiceImpl implements SatelliteCloudChartService {

    private final static String URL = "http://www.wztf121.com/product/Redar/cloud_fy4a_colour_12.json?v=";

    @Override
    public JSONArray satelliteCloudChart() {
        String url = URL + System.currentTimeMillis();
        String result = HttpClientUtil.httpGet(url);
        return JSONArray.parseArray(result);
    }
}
