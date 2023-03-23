package com.ygkj.big.screen.service;

import com.ygkj.big.screen.model.BusApiBase;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author fml
 * @date 2021-04-01 16:05
 * @description 水利数据仓专题
 */
public interface WaterDataWarehouseService {

    /**
     * 河流湖泊数量统计
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> riverLakeStats();

    /**
     * 水位、雨量测站数量统计
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> waterAndRainStaStats();

    /**
     * 水功能区、取水户数量统计 att_wfz_base
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> waterFunctionAndUseStats();

    /**
     * 水利业务数据统计 bus_data_divide
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> wcbdStats();

    /**
     * 数据审核状态统计
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<WuWiuStatsRespVo>> dataCheckStats();

    /**
     * 服务请求数量-近七天每天的服务申请数量
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> apiInvokeStats();

    /**
     * 近七日每天的入仓数据量
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> dataPersistentStats();

    /**
     * 服务分类统计 bus_api_base -- api_category
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> apiCategoryStats();

    /**
     * 接口报错次数排名 bus_api_invoke_log -- code 不为 0 或 200
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> apiErrorStats();

    /**
     * 最新上线服务 bus_api_base create_time desc
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<BusApiBase>> apiList();

    /**
     * 服务调用次数
     *
     * @return com.ygkj.water.model.CommonResult
     */
    CommonResult<List<DataStatisticsInfo>> apiInvokeTimeStats();
}
