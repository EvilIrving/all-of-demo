package com.ygkj.river.controller;

import com.ygkj.river.AttBeautLakesControllerApi;
import com.ygkj.river.service.AttBeautLakesService;
import com.ygkj.river.vo.request.AttBeautLakesReqVo;
import com.ygkj.river.vo.response.AttBeautLakesRespVo;
import com.ygkj.river.vo.response.BeautLakeRespVo;
import com.ygkj.river.vo.response.EachProjectStatisticsRespVO;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hucong
 * @description 美丽河湖表
 * @date 2021-08-12
 */
@RestController
@RequestMapping(value = "/AttBeautLakes")
public class AttBeautLakesController implements AttBeautLakesControllerApi {

    @Resource
    private AttBeautLakesService attBeautLakesService;

    @PostMapping("loadPage")
    @Override
    public CommonResult<PageVO<AttBeautLakesRespVo>> loadPage(AttBeautLakesReqVo vo) {
        return CommonResult.success(attBeautLakesService.loadPage(vo));
    }

    @GetMapping("projectStatic")
    @Override
    public CommonResult<BeautLakeRespVo> projectStatic(String type) {
        return CommonResult.success(attBeautLakesService.projectStatic(type));
    }

    @GetMapping("eachProjectStatistics")
    @Override
    public CommonResult<EachProjectStatisticsRespVO> eachProjectStatistics(String type) {
        return CommonResult.success(attBeautLakesService.eachProjectStatistics(type));
    }

}
