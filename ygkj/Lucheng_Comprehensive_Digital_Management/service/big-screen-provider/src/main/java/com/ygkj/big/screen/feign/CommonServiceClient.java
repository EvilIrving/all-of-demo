package com.ygkj.big.screen.feign;

import com.ygkj.big.screen.model.ReservoirCapacityCurve;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/4/28
 */
@FeignClient(contextId = "common-service", name = "common-service")
public interface CommonServiceClient {
    @PostMapping("/reservoirCapacityCurve/calculationCapacity")
    CommonResult<Map<String, BigDecimal>> calculationCapacity(@RequestBody List<ReservoirCapacityCurve> orgList);
}
