package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.WaterGuaranteeControllerApi;
import com.ygkj.big.screen.service.WaterGuaranteeService;
import com.ygkj.entity.SingletonMap;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xq
 * @Description
 * @Date 2021/7/29
 */
@RestController
@RequestMapping("waterGuarantee")
public class WaterGuaranteeController implements WaterGuaranteeControllerApi {

    @Autowired
    WaterGuaranteeService waterGuaranteeService;

    @GetMapping("stats4WrbRainfall")
    @Override
    public CommonResult stats4WrbRainfall(Integer year) {
        return CommonResult.success(waterGuaranteeService.stats4WrbRainfall(year));
    }

    @GetMapping("statsCwindow4WrbRainfall")
    @Override
    public CommonResult statsCwindow4WrbRainfall(Integer year) {
        return CommonResult.success(waterGuaranteeService.statsCwindow4WrbRainfall(year));
    }

    @GetMapping("table4WrbRainfall")
    @Override
    public CommonResult table4WrbRainfall(Integer year, String orderColumn, Boolean ascOrDesc) {
        return CommonResult.success(waterGuaranteeService.table4WrbRainfall(year, orderColumn, ascOrDesc));
    }

    @GetMapping("stats4WrbWra")
    @Override
    public CommonResult stats4WrbWra(Integer year, Integer type) {
        return CommonResult.success(waterGuaranteeService.stats4WrbWra(year, type));
    }

    @GetMapping("statsCwindow4WrbWra")
    @Override
    public CommonResult statsCwindow4WrbWra(Integer year) {
        return CommonResult.success(waterGuaranteeService.statsCwindow4WrbWra(year));
    }

    @GetMapping("table4WrbWra")
    @Override
    public CommonResult table4WrbWra(Integer year, String orderColumn, Boolean ascOrDesc) {
        return CommonResult.success(waterGuaranteeService.table4WrbWra(year, orderColumn, ascOrDesc));
    }

    @GetMapping("stats4WrbWaterSupply")
    @Override
    public CommonResult stats4WrbWaterSupply(Integer year, Integer type) {
        return CommonResult.success(waterGuaranteeService.stats4WrbWaterSupply(year, type));
    }

    @GetMapping("statsCwindow4WrbWaterSupply")
    @Override
    public CommonResult statsCwindow4WrbWaterSupply(Integer year) {
        return CommonResult.success(waterGuaranteeService.statsCwindow4WrbWaterSupply(year));
    }

//    @GetMapping("table4WrbWaterSupply")
//    @Override
//    public CommonResult table4WrbWaterSupply(Integer year,String orderColumn,Boolean ascOrDesc) {
//        return CommonResult.success(waterGuaranteeService.table4WrbWaterSupply(year,orderColumn,ascOrDesc));
//    }

    @GetMapping("stats4WrbWaterConsume")
    @Override
    public CommonResult stats4WrbWaterConsume(Integer year, Integer type) {
        return CommonResult.success(waterGuaranteeService.stats4WrbWaterConsume(year, type));
    }

    @GetMapping("statsCwindow4WrbWaterConsume")
    @Override
    public CommonResult statsCwindow4WrbWaterConsume(Integer year) {
        return CommonResult.success(waterGuaranteeService.statsCwindow4WrbWaterConsume(year));
    }

    @GetMapping("table4WrbWaterConsume")
    @Override
    public CommonResult table4WrbWaterConsume(Integer year, String orderColumn, Boolean ascOrDesc) {
        return CommonResult.success(waterGuaranteeService.table4WrbWaterConsume(year, orderColumn, ascOrDesc));
    }

    @GetMapping("stats4WrbRsvrStore")
    @Override
    public CommonResult stats4WrbRsvrStore(Integer year) {
        return CommonResult.success(waterGuaranteeService.stats4WrbRsvrStore(year));
    }

    @GetMapping("statsCwindow4WrbRsvrStore")
    @Override
    public CommonResult statsCwindow4WrbRsvrStore(Integer year) {
        return CommonResult.success(waterGuaranteeService.statsCwindow4WrbRsvrStore(year));
    }

    @GetMapping("table4WrbRsvrStore")
    @Override
    public CommonResult table4WrbRsvrStore(Integer year, String orderColumn, Boolean ascOrDesc) {
        return CommonResult.success(waterGuaranteeService.table4WrbRsvrStore(year, orderColumn, ascOrDesc));
    }

    @GetMapping("curve4WrbRsvrStore")
    @Override
    public CommonResult curve4WrbRsvrStore(String resCode) {
        return CommonResult.success(waterGuaranteeService.curve4WrbRsvrStore(resCode));
    }

    @GetMapping("stats4WraUnit")
    @Override
    public CommonResult stats4WraUnit(Integer type) {
        return waterGuaranteeService.stats4WraUnit(type);
    }

    @GetMapping("statsCwindow4WraUnit")
    @Override
    public CommonResult statsCwindow4WraUnit() {
        return CommonResult.success(waterGuaranteeService.statsCwindow4WraUnit());
    }

    @GetMapping("stats4SectFlow")
    @Override
    public CommonResult stats4SectFlow() {
        return waterGuaranteeService.stats4SectFlow();
    }

    @GetMapping("rsvrWatSrcTable")
    @Override
    public CommonResult rsvrWatSrcTable(String watSrcName) {
        return waterGuaranteeService.rsvrWatSrcTable(watSrcName);
    }

    @GetMapping("rsvrWatSrc4CWindow")
    @Override
    public CommonResult rsvrWatSrc4CWindow() {
        return waterGuaranteeService.rsvrWatSrc4CWindow();
    }

    @GetMapping("watSavNormByYearInterval")
    @Override
    public CommonResult watSavNormByYearInterval(String adcd, Integer startYear, Integer endYear) {
        return waterGuaranteeService.watSavNormByYearInterval(adcd, startYear, endYear);
    }

    @GetMapping("watSavNormByYear")
    @Override
    public CommonResult watSavNormByYear(Integer year, Integer compareType) {
        return waterGuaranteeService.watSavNormByYear(year, compareType);
    }

    @GetMapping("watSavNorm4CWindow")
    @Override
    public CommonResult watSavNorm4CWindow() {
        return waterGuaranteeService.watSavNorm4CWindow();
    }

    @GetMapping("wpgEffect")
    @Override
    public CommonResult wpgEffect(String areaLevel, Integer year) {
        return waterGuaranteeService.wpgEffect(areaLevel, year);
    }

    @GetMapping("wpgEffect4CWindow")
    @Override
    public CommonResult wpgEffect4CWindow() {
        return waterGuaranteeService.wpgEffect4CWindow();
    }

    @GetMapping("redLineWatCsm")
    @Override
    public CommonResult redLineWatCsm(Integer year) {
        return waterGuaranteeService.redLineWatCsm(year);
    }

    @GetMapping("redLineWpgEffect")
    @Override
    public CommonResult redLineWpgEffect(Integer year) {
        return waterGuaranteeService.redLineWpgEffect(year);
    }

    @GetMapping("redLine4CWindow")
    @Override
    public CommonResult redLine4CWindow() {
        return waterGuaranteeService.redLine4CWindow();
    }

    @GetMapping("intakeSuperviseStats")
    @Override
    public CommonResult intakeSuperviseStats(@RequestParam("type") int type) {
        return waterGuaranteeService.intakeSuperviseStats(type);
    }

    @GetMapping("intakeSupervise4CWindow")
    @Override
    public CommonResult intakeSupervise4CWindow() {
        return waterGuaranteeService.intakeSupervise4CWindow();
    }

    @GetMapping("wiuStats")
    @Override
    public CommonResult wiuStats(@RequestParam("type") int type) {
        return waterGuaranteeService.wiuStats(type);
    }

    @GetMapping("wiuUnit4CWindow")
    @Override
    public CommonResult wiuUnit4CWindow() {
        return waterGuaranteeService.wiuUnit4CWindow();
    }
}
