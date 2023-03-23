package com.ygkj.water.project.controller;

import com.ygkj.project.BmSeawallStructinfoControllerApi;
import com.ygkj.project.model.BmSeawallStructinfo;
import com.ygkj.project.vo.request.BmSeawallStructinfoReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.BmSeawallStructinfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 9:15
 */
@RequestMapping("bmseawallstructinfo")
@RestController
@Api(tags = "海塘主体（桩号）")
public class BmSeawallStructinfoController implements BmSeawallStructinfoControllerApi {

    @Autowired
    private BmSeawallStructinfoService bmSeawallStructinfoService;

    @Override
    @PostMapping("/loadPage")
    @ApiOperation(value = "海塘主体（桩号）信息")
    public CommonResult loadPage(BmSeawallStructinfoReqVo reqVo) {
        return CommonResult.success(bmSeawallStructinfoService.loadPage(reqVo));
    }
}
