package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.drill.RainfallDrillManager;
import com.ygkj.big.screen.drill.notify.event.MountainTorrentDrillCaseEvent;
import com.ygkj.big.screen.drill.notify.event.RainfallDrillCaseEvent;
import com.ygkj.big.screen.drill.notify.event.RainfallSurfaceImgCaseEvent;
import com.ygkj.big.screen.drill.notify.event.RsvrDrillCaseEvent;
import com.ygkj.big.screen.DrillControllerApi;
import com.ygkj.big.screen.service.DrillService;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/7/9
 */
@RestController
@RequestMapping("drill")
public class DrillController implements DrillControllerApi {

    @Autowired
    RainfallDrillManager rainfallDrillManager;

    @Autowired
    GenerateDistributionIDService idService;

    @Autowired
    DrillService drillService;

    @PostMapping("startDoDrill")
    @Override
    public CommonResult startDoDrill(String caseId, String unitName) {
        switch (unitName) {
            case "实时降雨":
                NotifyCenter.publishEvent(new RainfallDrillCaseEvent(caseId));
                break;
            case "实时降雨面雨量":
                NotifyCenter.publishEvent(new RainfallSurfaceImgCaseEvent(caseId));
                break;
            case "山洪预警":
                NotifyCenter.publishEvent(new MountainTorrentDrillCaseEvent(caseId));
                break;
            case "水库":
                NotifyCenter.publishEvent(new RsvrDrillCaseEvent(caseId));
                break;
        }
        return CommonResult.success();
    }


    @PostMapping("startDrillAffair")
    @Override
    public CommonResult startDrillAffair(String caseId) {
        return drillService.startDrillAffair(caseId);
    }

    @PostMapping("closeDrillAffair")
    @Override
    public CommonResult closeDrillAffair(String affairId) {
        return drillService.closeDrillAffair(affairId);
    }

    @GetMapping("listDrillCase")
    @Override
    public CommonResult listDrillCase(String unitName, Integer year, String caseType) {
        return CommonResult.success(drillService.listDrillCase(unitName, year, caseType));
    }
}
