package com.ygkj.digitization.controller;

import com.ygkj.digitization.HydrFcstControllerApi;
import com.ygkj.digitization.service.HydrFcstService;
import com.ygkj.digitization.vo.request.HydrFcstQueryVo;
import com.ygkj.digitization.vo.response.HydrFcstResVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@RestController
@RequestMapping("hydrFcst/")
public class HydrFcstController implements HydrFcstControllerApi {

    @Autowired
    HydrFcstService hydrFcstService;

    @GetMapping("pageHydrFcst")
    @Override
    public CommonResult pageHydrFcst(HydrFcstQueryVo queryVo) {
        return CommonResult.success(hydrFcstService.pageHydrFcst(queryVo));
    }

    @PostMapping("addOrUpdateHydrFcst")
    @Override
    public CommonResult addOrUpdateHydrFcst(@RequestBody HydrFcstResVo resVo) {
        return hydrFcstService.addOrUpdateHydrFcst(resVo);
    }

    @GetMapping("getHydrFcst")
    @Override
    public CommonResult getHydrFcst(String id) {
        return CommonResult.success(hydrFcstService.getHydrFcst(id));
    }

    @PostMapping("deleteHydrFcst")
    @Override
    public CommonResult deleteHydrFcst(String id) {
        return hydrFcstService.deleteHydrFcst(id);
    }

    @GetMapping("newestHydrFcst")
    @Override
    public CommonResult newestHydrFcst() {
        return CommonResult.success(hydrFcstService.newestHydrFcst());
    }

    @GetMapping("listHydrStation")
    @Override
    public CommonResult listHydrStation(String stType, String stName) {
        return CommonResult.success(hydrFcstService.listHydrStation(stType,stName));
    }
}
