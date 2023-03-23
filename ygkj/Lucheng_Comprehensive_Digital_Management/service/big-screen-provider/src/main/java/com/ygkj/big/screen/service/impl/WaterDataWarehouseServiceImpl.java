package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.service.WaterDataWarehouseService;
import com.ygkj.big.screen.mapper.WaterDataWarehouseMapper;
import com.ygkj.big.screen.model.BusApiBase;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fml
 * @date 2021-04-01 16:06
 * @description 水利数据仓专题
 */
@Service("waterDataWarehouseService")
public class WaterDataWarehouseServiceImpl implements WaterDataWarehouseService {

    @Resource
    private WaterDataWarehouseMapper waterDataWarehouseMapper;

    @Override
    public CommonResult<List<WuWiuStatsRespVo>> riverLakeStats() {

        List<WuWiuStatsRespVo> respVos = waterDataWarehouseMapper.riverLakeStats();
        return CommonResult.success(respVos);
    }

    @Override
    public CommonResult<List<WuWiuStatsRespVo>> waterAndRainStaStats() {

        List<WuWiuStatsRespVo> respVos = waterDataWarehouseMapper.waterAndRainStaStats();
        return CommonResult.success(respVos);
    }

    @Override
    public CommonResult<List<WuWiuStatsRespVo>> waterFunctionAndUseStats() {

        List<WuWiuStatsRespVo> respVos = waterDataWarehouseMapper.waterFunctionAndUseStats();
        return CommonResult.success(respVos);
    }

    @Override
    public CommonResult<List<WuWiuStatsRespVo>> wcbdStats() {

        List<WuWiuStatsRespVo> respVos = waterDataWarehouseMapper.wcbdStats();
        return CommonResult.success(respVos);
    }

    @Override
    public CommonResult<List<WuWiuStatsRespVo>> dataCheckStats() {

        List<WuWiuStatsRespVo> respVos = waterDataWarehouseMapper.dataCheckStats();
        return CommonResult.success(respVos);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> apiInvokeStats() {

        List<DataStatisticsInfo> statsInfo = waterDataWarehouseMapper.apiInvokeStats();
        return CommonResult.success(statsInfo);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> dataPersistentStats() {

        List<DataStatisticsInfo> statsInfo = waterDataWarehouseMapper.dataPersistentStats();
        return CommonResult.success(statsInfo);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> apiCategoryStats() {

        // 服务分类统计 bus_api_base -- api_category
        List<DataStatisticsInfo> statsInfo = waterDataWarehouseMapper.apiCategoryStats();
        return CommonResult.success(statsInfo);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> apiErrorStats() {

        // 接口报错次数排名 bus_api_invoke_log -- code 不为 0 或 200
        List<DataStatisticsInfo> statsInfo = waterDataWarehouseMapper.apiErrorStats();
        return CommonResult.success(statsInfo);
    }

    @Override
    public CommonResult<List<BusApiBase>> apiList() {

        // 最新上线服务 bus_api_base create_time desc
        List<BusApiBase> apiBases = waterDataWarehouseMapper.apiList();
        return CommonResult.success(apiBases);
    }

    @Override
    public CommonResult<List<DataStatisticsInfo>> apiInvokeTimeStats() {

        List<DataStatisticsInfo> statsInfo = waterDataWarehouseMapper.apiInvokeTimeStats();
        return CommonResult.success(statsInfo);
    }
}
