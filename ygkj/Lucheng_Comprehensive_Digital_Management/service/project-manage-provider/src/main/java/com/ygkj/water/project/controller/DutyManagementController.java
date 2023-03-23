package com.ygkj.water.project.controller;

import com.ygkj.project.model.DutyManagement;
import com.ygkj.project.vo.request.DutyReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.DutyManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "值班管理")
@RestController
@RequestMapping("/dutyManagement")
public class DutyManagementController {

    @Autowired
    private DutyManagementService dutyManagementService;

    @ApiOperation("模板下载")
    @GetMapping("/downloadTemplate")
    public void downloadTemplate(String yearAndMonth, HttpServletResponse response) {
        try {
            dutyManagementService.downloadTemplate(yearAndMonth,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ApiOperation("模板导入")
    @PostMapping("/importTemplate")
    public CommonResult importTemplate(MultipartFile file) {
        try {
            dutyManagementService.importTemplate(file);
            return CommonResult.success("导入成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed(e.getMessage());
        }

    }

    @ApiOperation("值班表列表")
    @GetMapping("/loadPage")
    public CommonResult loadPage(DutyReqVo reqVo) {

        return CommonResult.success(dutyManagementService.loadPage(reqVo));
    }

    @ApiOperation("编辑值班表")
    @GetMapping("/updateDuty")
    public CommonResult updateDuty(DutyReqVo reqVo) {
        DutyManagement dutyManagement = new DutyManagement();
        BeanUtils.copyProperties(reqVo,dutyManagement);
        if (dutyManagementService.updateById(dutyManagement)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }

    }

    @GetMapping("/test")
    void test(){

        dutyManagementService.sendDingMsgByTime();
    }

}
