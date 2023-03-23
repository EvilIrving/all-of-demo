package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterDataWarehouseControllerApi;
import com.ygkj.big.screen.model.BusApiBase;
import com.ygkj.big.screen.service.WaterDataWarehouseService;
import com.ygkj.big.screen.vo.response.DataStatisticsInfo;
import com.ygkj.big.screen.vo.response.WuWiuStatsRespVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fml
 * @date 2021-04-01 13:43
 * @description 水利数据仓专题
 */
@RestController
@RequestMapping("/waterDataWarehouse")
public class WaterDataWarehouseController implements WaterDataWarehouseControllerApi {

    @Autowired
    private WaterDataWarehouseService waterDataWarehouseService;

    // 十大工程统计--同防汛

    // 河流湖泊数量统计
    @Override
    @GetMapping("/riverLakeStats")
    public CommonResult<List<WuWiuStatsRespVo>> riverLakeStats() {

        return waterDataWarehouseService.riverLakeStats();
    }

    // 水位、雨量测站数量统计
    @Override
    @GetMapping("/waterAndRainStaStats")
    public CommonResult<List<WuWiuStatsRespVo>> waterAndRainStaStats() {

        return waterDataWarehouseService.waterAndRainStaStats();
    }

    // 水功能区、取水户数量统计 att_wfz_base
    @Override
    @GetMapping("/waterFunctionAndUseStats")
    public CommonResult<List<WuWiuStatsRespVo>> waterFunctionAndUseStats() {

        return waterDataWarehouseService.waterFunctionAndUseStats();
    }

    // 水利业务数据统计 bus_data_divide
    @Override
    @GetMapping("/wcbdStats")
    public CommonResult<List<WuWiuStatsRespVo>> wcbdStats() {

        return waterDataWarehouseService.wcbdStats();
    }

    // 数据审核状态统计
    @Override
    @GetMapping("/dataCheckStats")
    public CommonResult<List<WuWiuStatsRespVo>> dataCheckStats() {

        return waterDataWarehouseService.dataCheckStats();
    }

    // 服务请求数量-近七天每天的服务申请数量
    @Override
    @GetMapping("/apiInvokeStats")
    public CommonResult<List<DataStatisticsInfo>> apiInvokeStats() {

        return waterDataWarehouseService.apiInvokeStats();
    }

    // 近七日每天的入仓数据量
    @Override
    @GetMapping("/dataPersistentStats")
    public CommonResult<List<DataStatisticsInfo>> dataPersistentStats() {

        return waterDataWarehouseService.dataPersistentStats();
    }

    // 服务分类统计 bus_api_base -- api_category
    @Override
    @GetMapping("/apiCategoryStats")
    public CommonResult<List<DataStatisticsInfo>> apiCategoryStats() {

        return waterDataWarehouseService.apiCategoryStats();
    }

    // 接口报错次数排名 bus_api_invoke_log -- code 不为 0 或 200 (报错的最多的前五)
    @Override
    @GetMapping("/apiErrorStats")
    public CommonResult<List<DataStatisticsInfo>> apiErrorStats() {

        return waterDataWarehouseService.apiErrorStats();
    }

    // 最新上线服务 bus_api_base create_time desc
    @Override
    @GetMapping("/apiList")
    public CommonResult<List<BusApiBase>> apiList() {

        return waterDataWarehouseService.apiList();
    }

    // 服务调用次数
    @Override
    @GetMapping("/apiInvokeTimeStats")
    public CommonResult<List<DataStatisticsInfo>> apiInvokeTimeStats() {

        return waterDataWarehouseService.apiInvokeTimeStats();
    }
}
