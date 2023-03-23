package com.ygkj.visualization.controller;

import com.ygkj.visualization.TyphoonDataControllerApi;
import com.ygkj.visualization.model.TynTyphoonAffect;
import com.ygkj.visualization.service.RealTyphoonService;
import com.ygkj.visualization.service.TynTyphoonAffectService;
import com.ygkj.visualization.vo.request.RealTyphoonDataRequestVo;
import com.ygkj.visualization.vo.request.TyphoonAffectReqVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/typhoons")
public class TyphoonDataController implements TyphoonDataControllerApi {

    private RealTyphoonService realTyphoonService;

    private TynTyphoonAffectService tynTyphoonAffectService;

    public TyphoonDataController(@Autowired RealTyphoonService realTyphoonService, @Autowired TynTyphoonAffectService tynTyphoonAffectService) {
        this.realTyphoonService = realTyphoonService;
        this.tynTyphoonAffectService = tynTyphoonAffectService;
    }

    @PostMapping("")
    @Override
    public CommonResult typhoons(RealTyphoonDataRequestVo realTyphoonDataRequestVo) {
        return CommonResult.success(realTyphoonService.selectByCondition(realTyphoonDataRequestVo));
    }

    @PostMapping("historyAndCurrent")
    @Override
    public CommonResult historyAndCurrent(RealTyphoonDataRequestVo realTyphoonDataRequestVo) {
        return CommonResult.success(realTyphoonService.historyAndCurrent(realTyphoonDataRequestVo));
    }

    @GetMapping("newestTyphoon")
    @Override
    public CommonResult newestTyphoon() {
        return CommonResult.success(realTyphoonService.newestTyphoon());
    }

    @GetMapping("/detail/{code}")
    @Override
    public CommonResult selectTyphoonDetail(@PathVariable String code) {
        return CommonResult.success(realTyphoonService.selectDetail(code));
    }
    @GetMapping("/detailById/{id}")
    @Override
    public CommonResult selectTyphoonDetailById(@PathVariable Integer id) {
        return CommonResult.success(realTyphoonService.selectDetailById(id));
    }

    @Override
    @PostMapping("/typhoonAffects")
    public CommonResult<PageVO<TynTyphoonAffect>> typhoonAffects(TyphoonAffectReqVo vo){

        return tynTyphoonAffectService.typhoonAffects(vo);
    }

    @GetMapping("/analysis")
    @Override
    public CommonResult analysis(String code, String type) {
        return CommonResult.success(realTyphoonService.analysis(code, type));
    }

    @GetMapping("/distance")
    @Override
    public CommonResult distanceFarFromWenZhou(String longitude, String latitude) {
        return CommonResult.success(realTyphoonService.distanceFarFromWenZhou(longitude, latitude));
    }

    @GetMapping("/getAdDistanceFromTyphoon")
    @Override
    public CommonResult getAdDistanceFromTyphoon(@RequestParam("longitude") Double longitude, @RequestParam("latitude")Double latitude) {
        return CommonResult.success(realTyphoonService.getAdDistanceFromTyphoon(longitude, latitude));
    }

    @GetMapping("/typhoon70PerArea")
    @Override
    public CommonResult typhoon70PerArea(String code) {
        return CommonResult.success(realTyphoonService.typhoon70PerArea(code));
    }

    @GetMapping("/currentTyphoon")
    @Override
    public CommonResult currentTyphoon() {
        return CommonResult.success(realTyphoonService.fuckingCurrentTyphoon());
    }

    @GetMapping("/typhoonStatistic")
    @Override
    public CommonResult typhoonStatistic(){
        return CommonResult.success(realTyphoonService.typhoonStatistic());
    }

    @GetMapping("typhoonStatisticRadarMap")
    @Override
    public CommonResult typhoonStatisticRadarMap(Integer year) {
        return CommonResult.success(realTyphoonService.typhoonStatisticRadarMap(year));
    }

    @GetMapping("landTimesEachAdcd")
    @Override
    public CommonResult landTimesEachAdcd() {
        return CommonResult.success(realTyphoonService.landTimesEachAdcd());
    }
}
