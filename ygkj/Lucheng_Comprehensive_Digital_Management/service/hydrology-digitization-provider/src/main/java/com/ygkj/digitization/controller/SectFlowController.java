package com.ygkj.digitization.controller;

import com.ygkj.digitization.SectFlowControllerApi;
import com.ygkj.digitization.service.SectFlowService;
import com.ygkj.digitization.vo.response.SectFlowQueryReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/7/31
 */
@RestController
@RequestMapping("sectFlow")
public class SectFlowController implements SectFlowControllerApi {

    @Autowired
    SectFlowService sectFlowService;

    @GetMapping("getSectFlowList")
    @Override
    public CommonResult getSectFlowList(SectFlowQueryReqVo reqVo) {
        return CommonResult.success(sectFlowService.getSectFlowList(reqVo));
    }

    @GetMapping("stats4SectFlowList")
    @Override
    public CommonResult stats4SectFlowList(SectFlowQueryReqVo reqVo) {
        return CommonResult.success(sectFlowService.stats4SectFlowList(reqVo));
    }

    @PostMapping("exportSectFlowList")
    @Override
    public void exportSectFlowList(@RequestBody SectFlowQueryReqVo reqVo, HttpServletResponse response) {
        sectFlowService.exportSectFlowList(reqVo,response);
    }

    @GetMapping("sectFlowStats")
    @Override
    public CommonResult sectFlowStats() {
        return CommonResult.success(sectFlowService.sectFlowStats());
    }

    @GetMapping("sectFlowCurve")
    @Override
    public CommonResult sectFlowCurve(String sectName, Integer hours) {
        return CommonResult.success(sectFlowService.sectFlowCurve(sectName,hours));
    }
}
