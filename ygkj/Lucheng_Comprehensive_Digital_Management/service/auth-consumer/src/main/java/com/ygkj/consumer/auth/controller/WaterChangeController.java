package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.ChangeDetailUpdateReqVo;
import com.ygkj.auth.VO.request.WaterChangeDetailAddReqVo;
import com.ygkj.auth.VO.request.WaterChangePageReqVo;
import com.ygkj.auth.VO.request.WaterChangeYearReqVo;
import com.ygkj.auth.api.monitor.WaterChangeManagementApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: fml
 * @Date: 2020/9/11 16:21
 * @Description:
 */
@RestController
@RequestMapping("/waterChangeManage")
public class WaterChangeController implements WaterChangeManagementApi {

    @Autowired
    private AuthenticationClient waterMonitorClient;

    @PostMapping("/queryWaterChangeInfo")
    @Override
    public Object queryWaterChangeInfo(WaterChangePageReqVo vo) {
        return waterMonitorClient.queryWaterChangeInfo(vo);
    }

    @GetMapping("/getChangeDetails/{id}")
    @Override
    public Object getChangeDetails(@PathVariable("id") String id) {
        return waterMonitorClient.getChangeDetails(id);
    }

    @PostMapping("/addChangeDetail")
    @Override
    public Object addChangeDetail(WaterChangeDetailAddReqVo vo) {
        return waterMonitorClient.addChangeDetail(vo);
    }

    @GetMapping("/queryChangeDetail/{id}")
    @Override
    public Object queryChangeDetail(@PathVariable("id") String id) {

        return waterMonitorClient.queryChangeDetail(id);
    }

    @PutMapping("/updateChangeDetail")
    @Override
    public Object updateChangeDetail(ChangeDetailUpdateReqVo vo) {
        return waterMonitorClient.updateWaterChangeInfo(vo);
    }

    @DeleteMapping("/deleteChangeDetail/{id}")
    @Override
    public Object deleteChangeDetail(@PathVariable("id") String id) {
        return waterMonitorClient.deleteChangeDetail(id);
    }

    @GetMapping("/queryWaterDetail/{id}")
    @Override
    public Object queryWaterDetail(@PathVariable("id") String id) {

        return waterMonitorClient.queryWaterDetail(id);
    }

    @PostMapping("/queryYearWaterDetailInfo")
    @Override
    public Object queryYearWaterDetailInfo(WaterChangeYearReqVo vo) {

        return waterMonitorClient.queryYearWaterDetailInfo(vo);
    }
}
