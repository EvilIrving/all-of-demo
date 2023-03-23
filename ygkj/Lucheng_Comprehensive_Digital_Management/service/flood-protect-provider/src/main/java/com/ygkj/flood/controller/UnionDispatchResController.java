package com.ygkj.flood.controller;

import com.ygkj.flood.UnionDispatchResControllerApi;
import com.ygkj.flood.service.UnionDispatchResService;
import com.ygkj.flood.vo.response.ProResInfo;
import com.ygkj.flood.vo.response.WlaOverWarnNumRespVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resInfo")
public class UnionDispatchResController implements UnionDispatchResControllerApi {

    @Autowired
    private UnionDispatchResService unionDispatchResService;

    // 大中型水库超汛限统计
    @Override
    @GetMapping("/warnNum")
    public CommonResult<WlaOverWarnNumRespVo> warnNum() {

        return unionDispatchResService.warnNum();
    }

    // 大中型水库列表(projectCode 工程编码(单查))
    @Override
    @GetMapping("/resProList")
    public CommonResult<List<ProResInfo>> resProList(String projectCode) {

        return unionDispatchResService.resProList(projectCode);
    }

    @GetMapping("/newestFloodPoint")
    @Override
    public CommonResult newestFloodPoint(Integer pageNum, Integer pageSize) {
        return CommonResult.success(unionDispatchResService.newestFloodPoint(pageNum, pageSize));
    }

    @GetMapping("/warnStatsAll")
    @Override
    public CommonResult warnStatsAll() {
        Map<String, Object> result = new HashMap<>();
        result.putAll(unionDispatchResService.rainfallWarn());
        result.putAll(unionDispatchResService.riverWagaWarn());
        result.putAll(unionDispatchResService.largeMidRsvrWarn());
        result.putAll(unionDispatchResService.floodPointWarn());
        return CommonResult.success(result);
    }

    @GetMapping("/rainfallWarn")
    @Override
    public CommonResult rainfallWarn() {
        return CommonResult.success(unionDispatchResService.rainfallWarn());
    }

    @GetMapping("/riverWagaWarn")
    @Override
    public CommonResult riverWagaWarn() {
        return CommonResult.success(unionDispatchResService.riverWagaWarn());
    }

    @GetMapping("/largeMidRsvrWarn")
    @Override
    public CommonResult largeMidRsvrWarn() {
        return CommonResult.success(unionDispatchResService.largeMidRsvrWarn());
    }

    @GetMapping("/floodPointWarn")
    @Override
    public CommonResult floodPointWarn() {
        return CommonResult.success(unionDispatchResService.floodPointWarn());
    }
}
