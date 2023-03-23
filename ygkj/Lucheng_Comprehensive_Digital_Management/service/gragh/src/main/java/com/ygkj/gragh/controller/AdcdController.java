package com.ygkj.gragh.controller;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.gragh.AdcdControllerApi;
import com.ygkj.gragh.service.AdcdService;
import com.ygkj.gragh.vo.request.AdcdQueryVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adcd")
public class AdcdController implements AdcdControllerApi {

    @Autowired
    private AdcdService adcdService;

    @PostMapping("")
    @Override
    public CommonResult adcd(AdcdQueryVo adcdQueryVo) {
        return CommonResult.success(adcdService.adcd(adcdQueryVo));
    }

    @PostMapping("/selectAdnmByAdcdList")
    public CommonResult selectAdnmByAdcdList(@RequestBody JSONArray requestBody) {
        return CommonResult.success(adcdService.selectAdnmByAdcdList(requestBody.toJavaList(String.class)));
    }
}
