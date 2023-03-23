package com.ygkj.digitization.controller;

import com.ygkj.digitization.TideFcstControllerApi;
import com.ygkj.digitization.service.TideFcstService;
import com.ygkj.digitization.vo.request.FcstValReqVo;
import com.ygkj.digitization.vo.request.TideFcstQueryVo;
import com.ygkj.digitization.vo.response.TideFcstResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@RestController
@RequestMapping("tideFcst")
@Api(tags = "风暴潮")
public class TideFcstController implements TideFcstControllerApi {

    @Autowired
    TideFcstService tideFcstService;

    @GetMapping("pageTideFcst")
    @Override
    public CommonResult pageTideFcst(TideFcstQueryVo queryVo) {
        return CommonResult.success(tideFcstService.pageTideFcst(queryVo));
    }

    @PostMapping("addOrUpdateTideFcst")
    @Override
    public CommonResult addOrUpdateTideFcst(@RequestBody TideFcstResVo resVo) {
        return tideFcstService.addOrUpdateTideFcst(resVo);
    }

    @GetMapping("getTideFcst")
    @Override
    public CommonResult getTideFcst(String id) {
        return CommonResult.success(tideFcstService.getTideFcst(id));
    }

    @PostMapping("deleteTideFcst")
    @Override
    public CommonResult deleteTideFcst(String id) {
        return tideFcstService.deleteTideFcst(id);
    }

    @GetMapping("newestTideFcst")
    @Override
    public CommonResult newestTideFcst(Integer releaseType) {
        return CommonResult.success(tideFcstService.newestTideFcst(releaseType));
    }

    @GetMapping("manualFcstTideList")
    @Override
    public CommonResult manualFcstTideList() {
        return CommonResult.success(tideFcstService.manualFcstTideList());
    }

    @Override
    @GetMapping("listTileFcst/{fcstId}")
    public CommonResult listTileFcst(@PathVariable("fcstId") String fcstId) {
        return CommonResult.success(tideFcstService.listTileFcst(fcstId));
    }

    @Override
    @GetMapping("listTileFcstSt/{fcstId}")
    public CommonResult listTileFcstSt(@PathVariable("fcstId") String fcstId) {
        return CommonResult.success(tideFcstService.listTileFcstSt(fcstId));
    }

    @Override
    @PostMapping("listTileFcstVal")
    public CommonResult listTileFcstVal(FcstValReqVo reqVo) {
        return CommonResult.success(tideFcstService.listTileFcstVal(reqVo));
    }
}
