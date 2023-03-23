package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.service.AncillaryBuildingService;
import com.ygkj.water.project.utils.DESUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-15 15:44
 */
@Api(tags = "海塘附属建筑物")
@RestController
@RequestMapping("/ancillarybuilding")
public class AncillaryBuildingController{

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private AncillaryBuildingService ancillaryBuildingService;

    @PostMapping("/selectAncillaryBuilding")
    @ApiOperation("海塘附属建筑物列表")
    public CommonResult selectAncillaryBuilding(String projectCategory, String prcd) {
        return CommonResult.success(ancillaryBuildingService.selectAncillaryBuilding(projectCategory,prcd));
    }

}
