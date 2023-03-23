package com.ygkj.big.screen.feign;

import com.ygkj.digitization.vo.request.MrwlReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
@FeignClient(contextId = "hydrology-digitization", name = "hydrology-digitization")
public interface HydrologyDigitizationClient {

    @PostMapping("waterStatusReport/mountainRiverWaterLevel")
    CommonResult<List<LinkedHashMap>> mountainRiverWaterLevel(@RequestBody MrwlReqVo reqVo);

    @GetMapping("sectFlow/sectFlowStats")
    CommonResult sectFlowStats();
}
