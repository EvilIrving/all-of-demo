package com.ygkj.gragh.controller;

import com.ygkj.gragh.BasinControllerApi;
import com.ygkj.gragh.service.BasinService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basin")
public class BasinController implements BasinControllerApi {

    @Autowired
    private BasinService basinService;

    @GetMapping("")
    @Override
    public CommonResult basinList() {
        return CommonResult.success(basinService.basinList());
    }
}
