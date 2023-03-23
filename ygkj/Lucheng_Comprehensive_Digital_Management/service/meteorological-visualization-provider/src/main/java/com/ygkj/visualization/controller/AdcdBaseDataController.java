package com.ygkj.visualization.controller;

import com.ygkj.visualization.AdcdBaseDataControllerApi;
import com.ygkj.visualization.service.AdcdBaseDataService;
import com.ygkj.visualization.vo.response.AdcdBaseDataStatisticsRespVo;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adcdBaseData")
public class AdcdBaseDataController implements AdcdBaseDataControllerApi {

    @Autowired
    private AdcdBaseDataService adcdBaseDataService;

    // 区县信息统计(type 1人口，2经济，3防台联系人)
    @Override
    @GetMapping("/dataStatistics")
    public CommonResult<AdcdBaseDataStatisticsRespVo> dataStatistics(String adnm, String type) {

        if (StringUtils.isEmpty(type)) {
            return CommonResult.failed("参数缺失");
        }
        return adcdBaseDataService.dataStatistics(adnm, type);
    }

    @Override
    @GetMapping("/ecoPopuStatistics")
    public CommonResult ecoPopuStatistics(@RequestParam("adcd") String adcd, @RequestParam(value = "years",required = false) Integer years) {
        if (years==null){
            years=30;
        }
        return CommonResult.success(adcdBaseDataService.ecoPopuStatistics(adcd, years));
    }

    @Override
    @GetMapping("/ecoPopuStatisticsByYears")
    public CommonResult ecoPopuStatisticsByYears(@RequestParam(value = "years",required = false) Integer years) {
        if (years==null){
            years=30;
        }
        return CommonResult.success(adcdBaseDataService.ecoPopuStatisticsByYears(years));
    }
}
