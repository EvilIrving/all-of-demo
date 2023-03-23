package com.ygkj.river.controller;

import com.ygkj.river.AttBlueExceptionInformationControllerApi;
import com.ygkj.river.model.AttBlueExceptionInformation;
import com.ygkj.river.service.AttBlueExceptionInformationService;
import com.ygkj.river.vo.request.AttBlueExceptionInformationReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lxl
 * @create 2021-08-06 9:28
 * @description 异常监管条目
 */
@RestController
@RequestMapping(value = "/blueExInfo")
public class AttBlueExceptionInformationController implements AttBlueExceptionInformationControllerApi {

    private AttBlueExceptionInformationService exInfoService;

    public AttBlueExceptionInformationController(@Autowired AttBlueExceptionInformationService exInfoService) {
        this.exInfoService = exInfoService;
    }

    @Override
    @PostMapping("/addExInfo")
    public CommonResult addExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation) {

        if (exInfoService.addExceptionInformation(attBlueExceptionInformation)) {
            return CommonResult.success("操作成功");
        } else {
            return CommonResult.failed("操作失败");
        }

    }

    @Override
    @PostMapping("/updateExInfo")
    public CommonResult updateExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation) {

        if (exInfoService.updateExceptionInformation(attBlueExceptionInformation)) {
            return CommonResult.success("操作成功");
        } else {
            return CommonResult.failed("操作失败");
        }
    }

    @Override
    @GetMapping("/deleteExInfo")
    public CommonResult deleteExceptionInformation(String exceptionInformationIds) {

        if (exInfoService.deleteExceptionInformation(exceptionInformationIds)) {
            return CommonResult.success("操作成功");
        } else {
            return CommonResult.failed("操作失败");
        }
    }


    @Override
    @PostMapping("/exInfoList")
    public CommonResult exceptionInformationList(AttBlueExceptionInformationReqVo reqVo) {

        return CommonResult.success(exInfoService.exceptionInformationList(reqVo));
    }


    @Override
    @GetMapping("/upOrDown")
    public CommonResult updateSort(String upOrDown, String id) {
        if (exInfoService.updateSort(upOrDown, id)) {
            return CommonResult.success("操作成功");
        } else {
            return CommonResult.failed("操作失败");
        }
    }
}
