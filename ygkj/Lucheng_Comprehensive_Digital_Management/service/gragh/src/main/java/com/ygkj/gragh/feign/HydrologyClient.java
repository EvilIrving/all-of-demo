package com.ygkj.gragh.feign;

import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lxl
 * @create 2021-07-30 17:06
 * @description 雨量预警整合查询
 */
@FeignClient(contextId = "hydrology-digitization", name = "hydrology-digitization")
public interface HydrologyClient {

    /**
     * 暴雨警报
     *
     * @param requestVo
     * @return
     */
    @PostMapping("/rainfallReport/rainstormWarnForOne")
    CommonResult rainstormWarning(@RequestBody StationRequestVo requestVo);

    /**
     * 提供超警数量
     *
     * @param requestVo
     * @return
     */
    @PostMapping("/rainfallReport/rainstormWarnNumForOne")
    CommonResult rainstormWarnNumListForOne(@RequestBody StationRequestVo requestVo);

}
