package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.api.file.model.SysFileControllerApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ygkj.com.util.DateUtil;

import java.util.Date;

/**
 * @author Huang.zh
 * @date 2020/11/26 16:50
 * @Description:
 */
@RestController
@RequestMapping("/sysFile")
public class SysFileController implements SysFileControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @GetMapping("")
    @Override
    public CommonResult files(@RequestParam(value = "fileIds", required = false) String fileIds) {
        return authenticationClient.files(fileIds);
    }

    @PostMapping("archivalDataStatistics")
    @Override
    public CommonResult archivalDataStatistics() {
        return authenticationClient.archivalDataStatistics();
    }

    @PostMapping("statUploadFile")
    @Override
    public CommonResult statUploadFile(@RequestParam(value = "year", required = false) Integer year) {
        if (year == null) {
            year = Integer.parseInt(DateUtil.format(new Date(),"yyyy"));
        }
        return authenticationClient.statUploadFile(year);
    }

    @Override
    public CommonResult fileParameterType() {
        return null;
    }
}
