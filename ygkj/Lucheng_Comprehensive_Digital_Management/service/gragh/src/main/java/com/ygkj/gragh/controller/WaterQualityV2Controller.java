package com.ygkj.gragh.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.WaterQualityV2ControllerApi;
import com.ygkj.gragh.service.WaterQualityV2Service;
import com.ygkj.gragh.vo.request.RivQuaReqVo;
import com.ygkj.gragh.vo.response.ChartResVo;
import com.ygkj.gragh.vo.response.RivQuaResVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/5
 */
@RestController
@RequestMapping("waterQualityV2")
public class WaterQualityV2Controller implements WaterQualityV2ControllerApi {


    @Autowired
    WaterQualityV2Service waterQualityV2Service;

    @GetMapping("list")
    @Override
    public CommonResult<List<RivQuaResVo>> list(RivQuaReqVo reqVo) {
        return CommonResult.success(waterQualityV2Service.list(reqVo));
    }

    @GetMapping("stats4List")
    @Override
    public CommonResult<JSONObject> stats4List(RivQuaReqVo reqVo) {
        return CommonResult.success(waterQualityV2Service.stats4List(reqVo));
    }

    @PostMapping("exportList")
    @Override
    public void exportList(@RequestBody RivQuaReqVo reqVo, HttpServletResponse response) {
        waterQualityV2Service.exportList(reqVo, response);
    }

    @GetMapping("qualityCurve")
    @Override
    public CommonResult<List<RivQuaResVo>> qualityCurve(String siteNo, int month) {
        return CommonResult.success(waterQualityV2Service.qualityCurve(siteNo, month));
    }

    @GetMapping("qualityStats")
    @Override
    public CommonResult<List<ChartResVo<String, Long>>> qualityStats(@RequestParam("type") int type) {
        return CommonResult.success(waterQualityV2Service.qualityStats(type));
    }

    @GetMapping("qualityUnitCWindow")
    @Override
    public CommonResult qualityUnitCWindow() {
        return CommonResult.success(waterQualityV2Service.qualityUnitCWindow());
    }
}
