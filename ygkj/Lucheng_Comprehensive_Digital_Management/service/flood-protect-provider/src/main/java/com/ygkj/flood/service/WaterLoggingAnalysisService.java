package com.ygkj.flood.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.flood.model.AttFloodPoint;
import com.ygkj.flood.model.StBase;
import com.ygkj.flood.vo.request.FloodPointReqVo;
import com.ygkj.flood.vo.response.ChartResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/25
 */
public interface WaterLoggingAnalysisService {

    /**
     * 查询最新的积水点位，并且根据积水水深降序，并设置判断是否有积水
     *
     * @return
     */
    List<AttFloodPoint> listFloodPoint(FloodPointReqVo reqVo);

    /**
     * 积水点分页
     *
     * @param reqVo
     * @return
     */
    PageVO<AttFloodPoint> pageFloodPoint(FloodPointReqVo reqVo);

    /**
     * 积水点分页统计
     *
     * @param reqVo
     * @return
     */
    JSONObject stats4PageFloodPoint(FloodPointReqVo reqVo);

    /**
     * 积水点组件C位统计
     *
     * @param dataTime
     * @return
     */
    JSONObject floodPointCWindows(Date dataTime);

    /**
     * 积水点积水次数热力图
     *
     * @param dataTime
     * @return
     */
    List<AttFloodPoint> floodPointHeatMap(Date dataTime);

    /**
     * 查询历史积水曲线
     *
     * @param pointName 积水点名称
     * @param hours     时长，按小时计
     * @return
     */
    List<ChartResVo<Date, BigDecimal>> floodPointHistoricCurve(String pointName, Integer hours);

    /**
     * 查询最新最深的前几条积水点历史积水点曲线，
     *
     * @return
     */
    List<ChartResVo<String, List<ChartResVo<Date, BigDecimal>>>> floodPointCurveList(Integer limit);

    /**
     * 查询最新的积水点位，并且根据积水水深降序，并设置判断是否有积水
     *
     * @return
     */
    List<ChartResVo<Date, BigDecimal>> floodPointCurve(String pointName);

    /**
     * 积水点统计
     *
     * @param type 1. 根据行政区划统计 2. 根据流域统计
     */
    Object floodPointStatistic(int type);

    /**
     * @param name
     * @return
     */
    List<ChartResVo<Date, BigDecimal>> floodPointForecastCurve(String name);

    /**
     * @param name
     * @return
     */
    List<ChartResVo<Date, BigDecimal>> waterLoggingForecastCurve(String name);

    /**
     * 查询最新的河道水位，并且根据超预警值降雨，且设置是否超预警
     *
     * @return
     */
    List<StBase> riverWaterLevel(String basin);

    /**
     * 水闸工程水位数据列表
     *
     * @return
     */
    List<StBase> wagaList(String basin);

    /**
     * 洪涝风险
     *
     * @return
     */
    CommonResult floodRisk();

    /**
     * 内涝工情报警数量统计
     *
     * @return
     */
    JSONObject warnNum();
}
