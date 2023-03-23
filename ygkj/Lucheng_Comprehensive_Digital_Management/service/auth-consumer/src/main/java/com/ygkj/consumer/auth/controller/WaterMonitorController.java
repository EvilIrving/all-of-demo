package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.WaterBaseAddReqVo;
import com.ygkj.auth.VO.request.WaterBaseByInfoPageReqVo;
import com.ygkj.auth.VO.request.WaterBaseUpdateReqVo;
import com.ygkj.auth.api.monitor.WaterMonitorControllerApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: fml
 * @Date: 2020/9/8 17:32
 * @Description:
 */
@RestController
@RequestMapping("/waterbase")
public class WaterMonitorController implements WaterMonitorControllerApi {

    @Autowired
    private AuthenticationClient waterMonitorClient;

    @PostMapping("/insert")
    @Override
    public Object insert(WaterBaseAddReqVo vo) {
        return waterMonitorClient.insert(vo);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public Object delete(@PathVariable("id") String id) {
        return waterMonitorClient.delete(id);
    }

    @PutMapping("/update")
    @Override
    public Object update(WaterBaseUpdateReqVo vo) {
        return waterMonitorClient.update(vo);
    }

    @GetMapping("/load/{id}")
    @Override
    public Object load(@PathVariable("id") String id) {
        return waterMonitorClient.load(id);
    }

    @PostMapping("/pageListByInfo")
    @Override
    public Object pageListByInfo(WaterBaseByInfoPageReqVo vo) {
        return waterMonitorClient.pageListByInfo(vo);
    }

    @GetMapping("/queryAreaNames")
    @Override
    public Object queryAreaNames() {
        return waterMonitorClient.queryAreaNames();
    }

    @Override
    @PostMapping(value = "/parseExcel",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object parseExcel(MultipartFile file) {

        return waterMonitorClient.parseExcel(file);
    }

    @Override
    @PostMapping("/queryNearbyWaterBaseInfo")
    public Object queryNearbyWaterBaseInfo(@RequestParam("lng") Double lng, @RequestParam("lat") Double lat) {

        return waterMonitorClient.queryNearbyWaterBaseInfo(lng, lat);
    }
}
