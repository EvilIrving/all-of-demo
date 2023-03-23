package com.ygkj.gragh.rpc;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.*;
import com.ygkj.gragh.vo.request.SynRainRequestVo;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/18
 */
public interface GraphRpcService {

    /**
     * 大屏实时降雨统计
     *
     * @param areaName
     * @param endTime
     * @return
     */
    JSONObject rainfallStatistic(String areaName, String endTime);

    /**
     * 查询某时刻的水库水位
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StRsvrR> selectRsvrWaterLevelByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询某时刻的河道水位
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StRiverR> selectRiverByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询某时刻的水闸工情
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StWasR> selectWasByTimeCode(String table, List<String> stcds, Date dataTime);

    /**
     * 查询某时刻的潮位
     *
     * @param table    表名
     * @param stcds    测站编码 测站编码不能为空 否则会变为全表扫描
     * @param dataTime 数据时间 数据时间为空，则查询最新的
     * @return
     */
    List<StTideR> selectTideByTimeCode(String table, List<String> stcds, Date dataTime);
}
