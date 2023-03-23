package com.ygkj.river.controller;

import com.ygkj.river.AttBlueInspectionControllerApi;
import com.ygkj.river.feign.SupervisionFeignClient;
import com.ygkj.river.model.AttBlueInspection;
import com.ygkj.river.service.AttBlueInspectionService;
import com.ygkj.river.vo.request.AttBlueInspectionReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author lxl
 * @create 2021-08-06 9:25
 * @description 蓝线巡查内容
 */
@RestController
@RequestMapping(value = "/blueInspect")
public class AttBlueInspectionController implements AttBlueInspectionControllerApi {


    private AttBlueInspectionService inspectionService;
    private SupervisionFeignClient supervisionFeignClient;

    public AttBlueInspectionController(@Autowired AttBlueInspectionService inspectionService,
                                       @Autowired SupervisionFeignClient supervisionFeignClient) {
        this.inspectionService = inspectionService;
        this.supervisionFeignClient = supervisionFeignClient;
    }

    @Override
    @PostMapping("/addInspection")
    public CommonResult addInspection(AttBlueInspectionReqVo reqVo) {

        try {
            if (inspectionService.addInspection(reqVo)) {
                return CommonResult.success("操作成功");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }

        return CommonResult.failed("操作失败");
    }

    @Override
    @PostMapping("/updateInspection")
    public CommonResult updateInspection(AttBlueInspection attBlueInspection) {
        if (inspectionService.updateInspection(attBlueInspection)) {
            return CommonResult.success("操作成功");
        }

        return CommonResult.failed("操作失败");
    }


    @Override
    @GetMapping("/getDeptUserTree")
    public CommonResult getDeptUserTree(String key) {

        return CommonResult.success(inspectionService.getInspectDeptUser(key));
//        return supervisionFeignClient.getDeptTreeAboutUser(key);
    }


    @Override
    @PostMapping("/review")
    public CommonResult reviewDistribute(AttBlueInspectionReqVo reqVo) {

        if (inspectionService.reviewDistribute(reqVo)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }


    @Override
    @GetMapping("/hiddenDanger")
    public CommonResult hiddenDanger(String appraisalResult,String inspectionId, boolean flag) {
        if (inspectionService.hiddenDanger(appraisalResult,inspectionId,flag)){
            return CommonResult.success();
        }else {
            return CommonResult.failed();
        }
    }

    @Override
    @GetMapping("/list")
    public CommonResult inspectionList(String stage, String type, Integer pageSize, Integer pageNum, Integer result) {

        return CommonResult.success(inspectionService.inspectionList(stage, type, pageSize, pageNum, result));
    }

    @Override
    @GetMapping("/detail")
    public CommonResult inspectionDetail(String inspectionId) {

        return CommonResult.success(inspectionService.inspectionDetail(inspectionId));
    }

    @Override
    @PostMapping("/caseClosed")
    public CommonResult caseClosed(AttBlueInspectionReqVo reqVo) {
        return CommonResult.success(inspectionService.caseClosed(reqVo));
    }


    @Override
    @PostMapping("/generateRectificationOrder")
    public void generateRectificationOrder(HttpServletResponse response, @RequestBody Map<String,String> inspectionId) throws IOException {
        inspectionService.generateRectificationOrder(response,inspectionId);
    }
}
