package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.BusApiBase;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;

import java.util.List;

/**
 * @author fml
 * @date 2021-04-01 16:06
 * @description 水利数据仓专题
 */
public interface WaterDataWarehouseMapper {

    List<WuWiuStatsRespVo> riverLakeStats();

    List<WuWiuStatsRespVo> waterAndRainStaStats();

    List<WuWiuStatsRespVo> waterFunctionAndUseStats();

    @DS("soft")
    List<WuWiuStatsRespVo> wcbdStats();

    @DS("soft")
    List<WuWiuStatsRespVo> dataCheckStats();

    @DS("soft")
    List<DataStatisticsInfo> apiInvokeStats();

    @DS("soft")
    List<DataStatisticsInfo> dataPersistentStats();

    @DS("soft")
    List<DataStatisticsInfo> apiCategoryStats();

    @DS("soft")
    List<DataStatisticsInfo> apiErrorStats();

    @DS("soft")
    List<BusApiBase> apiList();

    @DS("soft")
    List<DataStatisticsInfo> apiInvokeTimeStats();
}
