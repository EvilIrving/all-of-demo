package com.ygkj.gragh.controller;

import com.ygkj.gragh.WaterSecurityControllerApi;
import com.ygkj.gragh.service.WaterSecurityService;
import com.ygkj.gragh.vo.request.KpakmaReqVo;
import com.ygkj.gragh.vo.request.WainReqVo;
import com.ygkj.gragh.vo.request.WiuReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

@RestController
@RequestMapping("/waterSecurity")
public class WaterSecurityController implements WaterSecurityControllerApi {

    @Resource
    private WaterSecurityService waterSecurityService;

    @Override
    @PostMapping("listKpakma")
    public CommonResult listKpakmaYear(KpakmaReqVo kpakmaReqVo) {
        if (kpakmaReqVo.getYear() == null) {
            kpakmaReqVo.setYear(LocalDate.now().getYear());
        }
        return CommonResult.success(waterSecurityService.listKpakma(kpakmaReqVo));
    }

    @Override
    @PostMapping("listWiu")
    public CommonResult listWiu(WainReqVo wainReqVo) {
        return CommonResult.success(waterSecurityService.listWiu(wainReqVo));
    }

    @Override
    @PostMapping("listWain")
    public CommonResult listWain(WainReqVo wainReqVo) {
        return CommonResult.success(waterSecurityService.listWain(wainReqVo));
    }

    @Override
    @PostMapping("listSwhs")
    public CommonResult listSwhs(WainReqVo wainReqVo) {
        return CommonResult.success(waterSecurityService.listSwhs(wainReqVo));
    }

    @Override
    @GetMapping("wiuBase")
    public CommonResult wiuBase(String wiuCode) {
        return CommonResult.success(waterSecurityService.wiuBase(wiuCode));
    }

    @Override
    @GetMapping("wiuPlan")
    public CommonResult wiuPlan(String wiuCode) {
        return CommonResult.success(waterSecurityService.wiuPlan(wiuCode));
    }

    @Override
    @GetMapping("wiuWpt")
    public CommonResult wiuWpt(String wiuCode) {
        return CommonResult.success(waterSecurityService.wiuWpt(wiuCode));
    }

    @Override
    @GetMapping("wiuRecord")
    public CommonResult wiuRecord(String wiuCode) {
        return CommonResult.success(waterSecurityService.wiuRecord(wiuCode));
    }

    @Override
    @PostMapping("wiuUse")
    public CommonResult wiuUse(WiuReqVo wiuReqVo) {
        return CommonResult.success(waterSecurityService.wiuUse(wiuReqVo));
    }

    @Override
    @GetMapping("wainBase")
    public CommonResult wainBase(String wainCode) {
        return CommonResult.success(waterSecurityService.wainBase(wainCode));
    }

    @Override
    @GetMapping("swhsBase")
    public CommonResult swhsBase(String swhsCode) {
        return CommonResult.success(waterSecurityService.swhsBase(swhsCode));
    }

    @Override
    @GetMapping("kpakmaBase")
    public CommonResult kpakmaBase(String kpakmaCode) {
        return CommonResult.success(waterSecurityService.kpakmaBase(kpakmaCode));
    }
}
