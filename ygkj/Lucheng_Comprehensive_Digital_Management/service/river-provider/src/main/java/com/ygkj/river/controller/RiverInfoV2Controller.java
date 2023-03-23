package com.ygkj.river.controller;

import com.ygkj.river.RiverInfoV2ControllerApi;
import com.ygkj.river.service.RiverInfoV2Service;
import com.ygkj.river.vo.request.RiverHealthStatReqVo;
import com.ygkj.river.vo.request.RiverQueryReqVo;
import com.ygkj.river.vo.response.NearestTheRiverRespVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/7
 */
@RestController
@RequestMapping("riverInfoV2")
public class RiverInfoV2Controller implements RiverInfoV2ControllerApi {

    @Autowired
    RiverInfoV2Service riverInfoV2Service;

    @GetMapping("pageRivSegBaseInfo")
    @Override
    public CommonResult pageRivSegBaseInfo(RiverQueryReqVo reqVo) {
        return CommonResult.success(riverInfoV2Service.pageRivSegBaseInfo(reqVo));
    }

    @GetMapping("pageRivSegStats")
    @Override
    public CommonResult pageRivSegStats(RiverQueryReqVo reqVo) {
        return CommonResult.success(riverInfoV2Service.pageRivSegStats(reqVo));
    }

    @GetMapping("riverCoordinate")
    @Override
    public CommonResult riverCoordinate(RiverQueryReqVo reqVo) {
        return CommonResult.success(riverInfoV2Service.riverCoordinate(reqVo));
    }

    @GetMapping("riverStats")
    @Override
    public CommonResult riverStats(Integer statsType) {
        return CommonResult.success(riverInfoV2Service.riverStats(statsType));
    }

    @GetMapping("nearestTheRiver")
    @Override
    public CommonResult<List<NearestTheRiverRespVo>> nearestTheRiver(String riverNo, Double latitude, Double longitude, String address) {
        return CommonResult.success(riverInfoV2Service.nearestTheRiver(riverNo, latitude, longitude, address));
    }

    @GetMapping("rivSegInfoById")
    @Override
    public CommonResult rivSegInfoById(Integer id) {
        return CommonResult.success(riverInfoV2Service.rivSegInfoById(id));
    }

    @PostMapping("updateRivSegHealthState")
    @Override
    public CommonResult updateRivSegHealthState(@RequestBody List<RiverHealthStatReqVo> reqVoList) {
        return riverInfoV2Service.updateRivSegHealthState(reqVoList);
    }

    @GetMapping("rivSegStaticByArea")
    @Override
    public CommonResult rivSegStaticByArea(String type) {
        return CommonResult.success(riverInfoV2Service.rivSegStaticByArea(type));
    }

    @GetMapping("rivSegStaticByAreaLevel")
    @Override
    public CommonResult rivSegStaticByAreaLevel(String type) {
        return CommonResult.success(riverInfoV2Service.rivSegStaticByAreaLevel(type));
    }

    @GetMapping("rivSegStaticByHealthState")
    @Override
    public CommonResult rivSegStaticByHealthState() {
        return CommonResult.success(riverInfoV2Service.rivSegStaticByHealthState());
    }

    @GetMapping("rivSegCStatic")
    @Override
    public CommonResult rivSegCStatic() {
        return CommonResult.success(riverInfoV2Service.rivSegCStatic());
    }
}
