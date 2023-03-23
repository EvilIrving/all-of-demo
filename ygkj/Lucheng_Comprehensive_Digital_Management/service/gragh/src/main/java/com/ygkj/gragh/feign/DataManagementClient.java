package com.ygkj.gragh.feign;

import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(contextId = "data-management", name = "data-management")
public interface DataManagementClient {

    @PostMapping("/stationRule/findStationOffline")
    CommonResult<List<Map<String, Object>>> findStationOffline(@RequestParam(value = "start", required = false) String start,
                                                               @RequestParam(value = "end", required = false) String end);
}
