package com.ygkj.digitization.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.vo.request.StationRequestVo;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Map;

public interface WaterReportService {

    LinkedList<Map<String, Object>> partitionRainfall(StationRequestVo stationRequestVo) throws ParseException;

    LinkedList<Object> periodExtremumRain(StationRequestVo requestVo);

    LinkedList<Map<String, Object>> dailyRainfall(StationRequestVo requestVo);

    JSONObject rainstormWarning(StationRequestVo requestVo);

    /**
     *  为一张图服务提供的接口
     * @param requestVo
     * @return
     */
    Object rainstormWarnForOne(StationRequestVo requestVo);

    /**
     * 导出日雨量
     * @param requestVo
     */
    void exportDailyRainfall(StationRequestVo requestVo, HttpServletResponse response);

    /**
     * 导出暴雨警报
     * @param requestVo
     * @param response
     */
    void exportRainstormWarning(StationRequestVo requestVo, HttpServletResponse response);

    /**
     * 导出极值报表
     * @param requestVo
     * @param response
     */
    void exportPeriodExtremumRain(StationRequestVo requestVo, HttpServletResponse response);
}
