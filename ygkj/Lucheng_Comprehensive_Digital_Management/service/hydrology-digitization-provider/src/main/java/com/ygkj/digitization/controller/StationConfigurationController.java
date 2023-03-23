package com.ygkj.digitization.controller;

import com.ygkj.digitization.StationConfigurationControllerApi;
import com.ygkj.digitization.service.StationService;
import com.ygkj.digitization.vo.request.AssoStationReservoirReqVo;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.response.AssoStationReservoirRespVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stationConfiguration")
public class StationConfigurationController implements StationConfigurationControllerApi {

    private StationService stationService;

    public StationConfigurationController(@Autowired StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/reservoirStationTree")
    @Override
    public CommonResult reservoirStationTree(String name) {
        return CommonResult.success(stationService.reservoirStationTree(name));
    }

    @PostMapping("/stations")
    @Override
    public CommonResult stations(StationQueryVo queryVo) {
        return CommonResult.success(stationService.stationList(queryVo));
    }

    @PostMapping("/focus")
    @Override
    public CommonResult focus(String stationCode,Boolean focus) {
        if (stationService.focus(stationCode, focus)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PostMapping("/display")
    @Override
    public CommonResult display(String stationCode, Boolean display) {
        if (stationService.display(stationCode, display)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PostMapping("/bindReservoirStation")
    @Override
    public CommonResult bindReservoirStation(String reservoirCode, String stationCode) {
        if (stationService.bindReservoirStation(reservoirCode,stationCode)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @Override
    public CommonResult bindBatchReservoirStation(String reservoirCode, String stationCodes) {
        stationService.bindBatchReservoirStation(reservoirCode,stationCodes);
        return CommonResult.success();
    }

    @PostMapping("/updateReservoirMainStation")
    @Override
    public CommonResult updateReservoirMainStation(String reservoirCode, String stationCode) {
        if (stationService.updateReservoirMainStation(reservoirCode,stationCode)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @PostMapping("assoStationReservoirList")
    @Override
    public CommonResult<PageVO<AssoStationReservoirRespVo>> assoStationReservoirList(AssoStationReservoirReqVo vo) {
        return CommonResult.success(stationService.assoStationReservoirList(vo));
    }

    @PostMapping("/reservoirRainfallSurfaceCalculate")
    @Override
    public CommonResult reservoirRainfallSurfaceCalculate(String stationCode, Boolean reservoirRainfallSurfaceCalculate) {
        if (stationService.reservoirRainfallSurfaceCalculate(stationCode,reservoirRainfallSurfaceCalculate)){
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @GetMapping("/unBind")
    @Override
    public CommonResult unBindReservoirStationInfo() {
        return CommonResult.success(stationService.unBindReservoirStationInfo());
    }
}
