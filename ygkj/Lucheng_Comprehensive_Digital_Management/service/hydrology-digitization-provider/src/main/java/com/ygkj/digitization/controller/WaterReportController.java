package com.ygkj.digitization.controller;

import com.ygkj.digitization.WaterReportControllerApi;
import com.ygkj.digitization.service.RainfallReportService;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 水位报表controller
 */

@RestController
@RequestMapping("waterReport")
public class WaterReportController implements WaterReportControllerApi {

    @Autowired
    private RainfallReportService rainfallReportService;

    @PostMapping("河网水位")
    @Override
    public CommonResult RiverwaterLevel(StationQueryVo queryVo) {
        return CommonResult.success(rainfallReportService.riverwaterLevel(queryVo));
    }


}