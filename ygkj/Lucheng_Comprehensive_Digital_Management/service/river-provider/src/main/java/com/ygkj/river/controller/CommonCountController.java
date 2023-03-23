package com.ygkj.river.controller;

import com.ygkj.river.CommonCountControllerApi;
import com.ygkj.river.service.CommonCountService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commonCount")
public class CommonCountController implements CommonCountControllerApi {

    private CommonCountService commonCountService;

    public CommonCountController(@Autowired CommonCountService commonCountService) {
        this.commonCountService = commonCountService;
    }

    @PostMapping("")
    @Override
    public CommonResult count(String type, String column) {
        return CommonResult.success(commonCountService.count(type, column));
    }
}
