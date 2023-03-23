package com.ygkj.water.project.controller;

import com.ygkj.project.RsvrControllerApi;
import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.project.vo.request.RsvrWatSrcQueryVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.RsvrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/8/11
 */
@RestController
@RequestMapping("rsvr")
public class RsvrController implements RsvrControllerApi {

    @Autowired
    RsvrService rsvrService;

    @GetMapping("listSafetyConclusion")
    @Override
    public CommonResult listSafetyConclusion() {
        return CommonResult.success(rsvrService.listSafetyConclusion());
    }

    @PostMapping("rsvrUnitList")
    @Override
    public CommonResult rsvrUnitList(@RequestBody RsvrStationWaterReqVo reqVo) {
        return CommonResult.success(rsvrService.rsvrUnitList(reqVo));
    }

    @PostMapping("stats4RsvrUnitList")
    @Override
    public CommonResult stats4RsvrUnitList(@RequestBody RsvrStationWaterReqVo reqVo) {
        return CommonResult.success(rsvrService.stats4RsvrUnitList(reqVo));
    }

    @PostMapping("exportRsvr")
    public void exportRsvr(HttpServletResponse response,@RequestBody RsvrStationWaterReqVo reqVo){
        rsvrService.exportRsvr(response,reqVo);
    }

    @GetMapping("rsvrWatSrcTable")
    @Override
    public CommonResult rsvrWatSrcTable(RsvrWatSrcQueryVo queryVo) {
        return CommonResult.success(rsvrService.rsvrWatSrcTable(queryVo.getWatSrcName()));
    }

    @GetMapping("stats4rsvrWatSrcTable")
    @Override
    public CommonResult stats4rsvrWatSrcTable(RsvrWatSrcQueryVo queryVo) {
        return CommonResult.success(rsvrService.stats4rsvrWatSrcTable(queryVo.getWatSrcName()));
    }

    @GetMapping("rsvrWatSrc4CWindow")
    @Override
    public CommonResult rsvrWatSrc4CWindow() {
        return CommonResult.success(rsvrService.rsvrWatSrc4CWindow());
    }

    @PostMapping("exportRsvrWatSrc")
    @Override
    public void exportRsvrWatSrc(@RequestBody RsvrWatSrcQueryVo queryVo, HttpServletResponse response) {
        rsvrService.exportRsvrWatSrc(queryVo, response);
    }
}
