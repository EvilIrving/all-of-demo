package com.ygkj.digitization.feign;

import com.ygkj.digitization.model.ReservoirCapacityCurve;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(contextId = "common-service", name = "common-service")
public interface CommonClient {

    @PostMapping("/reservoirCapacityCurve/calculationCapacity")
    CommonResult<Map<String, BigDecimal>> calculationCapacity(@RequestBody List<ReservoirCapacityCurve> orgList);
}
