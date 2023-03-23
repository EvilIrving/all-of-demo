package com.ygkj.water.project.controller;

import com.ygkj.project.WaterExpertControllerApi;
import com.ygkj.project.model.DtsRaBasicDataDbWaterExpert;
import com.ygkj.project.vo.request.WaterExpertReqVo;
import com.ygkj.project.vo.response.WaterExpertResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.impl.WaterExpertService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 14:21
 */
@RestController
@RequestMapping("/waterexpert")
public class WaterExpertController implements WaterExpertControllerApi {
    @Resource
    private WaterExpertService waterExpertService;

    @Override
    @PostMapping("listWaterExpert")
    public CommonResult<PageVO<WaterExpertResVo>> listWaterExpert(WaterExpertReqVo reqVo) {
        return CommonResult.success(waterExpertService.listWaterExpert(reqVo));
    }

    @Override
    @PostMapping("editWaterExpert")
    public CommonResult editWaterExpert(WaterExpertResVo waterExpert) {
        waterExpertService.editWaterExpert(waterExpert);
        return CommonResult.success("");
    }

    @Override
    @DeleteMapping("delWaterExpert/{dtsCmfId}")
    public CommonResult delWaterExpert(@PathVariable("dtsCmfId") String dtsCmfId) {
        waterExpertService.delWaterExpert(dtsCmfId);
        return CommonResult.success("");
    }

    @Override
    @GetMapping("selectWaterExpert")
    public CommonResult<DtsRaBasicDataDbWaterExpert> selectWaterExpert(String dtsCmfId) {
        return CommonResult.success(waterExpertService.selectWaterExpert(dtsCmfId));
    }

    @Override
    @PostMapping("countWaterExpert")
    public CommonResult countWaterExpert(WaterExpertReqVo reqVo) {
        return CommonResult.success(waterExpertService.countWaterExpert(reqVo));
    }
}
