package com.ygkj.water.project.controller;

import com.ygkj.project.BmAccBuildControllerApi;
import com.ygkj.project.model.BmAccBuild;
import com.ygkj.project.vo.response.BmAccBuildResVo;
import com.ygkj.water.model.CommonResult;

import com.ygkj.water.project.service.BmAccBuildService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-11 13:52
 */
@RestController
@RequestMapping("/bmAccBuild")
public class BmAccBuildController implements BmAccBuildControllerApi {
    @Autowired
    private BmAccBuildService bmAccBuildService;


    @PostMapping("/bmAccBuildList")
    @ApiOperation("附属建筑物列表")
    @Override
    public CommonResult selectBmAccBuildByCondition(String proType,String prcd) {
        List<BmAccBuildResVo> list= bmAccBuildService.selectBmAccBuildByCondition(proType,prcd);
        return CommonResult.success(list);
    }



}
