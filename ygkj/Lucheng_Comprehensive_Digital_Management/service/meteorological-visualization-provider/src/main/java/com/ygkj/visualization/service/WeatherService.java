package com.ygkj.visualization.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface WeatherService {

    JSONObject currentWeather(String areaName) throws Exception;

    JSONArray weatherPredict(String areaName) throws Exception;

    JSONArray wholeWeather();

    List<JSONObject> weatherStatistic(String areaName,String type) throws Exception;

    JSONObject rainfallWaterFromSiChuang(String longitude, String latitude);

    JSONArray caculateAllRainStationRainfall(Integer hour);

    JSONObject caculateRainfall(String longitude, String latitude);

    JSONObject rainAnalysis(String year,String month);

    JSONArray rainfallPredict();

    JSONArray situationAnalysis();

    List<JSONObject> surfaceRainfall();

    JSONObject predictRainfallStatistic();

    boolean updateRainfallPredictMap();

    /**
     * 根据行政区划统计面雨量
     * @param areaName
     * @return
     */
    JSONObject predictRainfallByAreaName(String areaName);

    JSONObject rainfallMaxValuePredict(Integer hour);

    JSONArray rainfallPredictGrid(String type);

    /**
     * 降雨预报详细表格导出
     * @param type 统计类型：行政区划/流域
     * @param response
     * @return
     */
    void exportRainfallPredictGrid(String type, HttpServletResponse response);

    JSONObject rainfallPredictByIndex(int rowIndex,int columnIndex);

    List<JSONObject> listJsonForWeather(String type);


    void insertPredictHourRain4FloodPredict();

    /**
     * 初始化测站格点
     */
    void initStGrid();

    Object rainFallPredictForWz();

    /**
     * 获取降雨预报区县
     * @param hour
     * @return
     */
    Object rainFallGridForWz(Integer hour);
}
