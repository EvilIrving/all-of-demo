package com.ygkj.gragh.feign;

import com.ygkj.gragh.model.RivernetCapacityCurve;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/rivernetCapacityCurve/calculationCapacity")
    CommonResult<Map<String, BigDecimal>> calculationRivernetCapacity(@RequestBody List<RivernetCapacityCurve> orgList);
}
