package com.ygkj.river.controller;

import com.ygkj.river.AttWaterareaCompensationControllerApi;
import com.ygkj.river.service.AttWaterareaCompensationService;
import com.ygkj.river.vo.response.AttWaterareaCompensationRespVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hucong
 * @description 水域占用补偿表
 * @date 2021-08-12
 */
@RestController
@RequestMapping(value = "/AttWaterareaCompensation")
public class AttWaterareaCompensationController implements AttWaterareaCompensationControllerApi {

    @Resource
    private AttWaterareaCompensationService attWaterareaCompensationService;

    @GetMapping("statisticsReclaimedAreaWater")
    @Override
    public CommonResult<AttWaterareaCompensationRespVo> statisticsReclaimedAreaWater(Integer year) {
        return CommonResult.success(attWaterareaCompensationService.statisticsReclaimedAreaWater(year));
    }

    @GetMapping("statisticsReclaimedByYear")
    @Override
    public CommonResult statisticsReclaimedByYear() {
        return CommonResult.success(attWaterareaCompensationService.statisticsReclaimedByYear());
    }

    @GetMapping("waterCompensationEachAdministrative")
    @Override
    public CommonResult<List<AttWaterareaCompensationRespVo>> waterCompensationEachAdministrative(Integer year) {
        return CommonResult.success(attWaterareaCompensationService.waterCompensationEachAdministrative(year));
    }
}
