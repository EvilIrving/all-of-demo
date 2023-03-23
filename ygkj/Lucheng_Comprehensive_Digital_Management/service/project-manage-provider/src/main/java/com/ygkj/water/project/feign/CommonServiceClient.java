package com.ygkj.water.project.feign;

import com.ygkj.flood.model.ReservoirCapacityCurve;
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
 * @Date 2021/5/12
 */
@FeignClient(contextId = "common-service", name = "common-service")
public interface CommonServiceClient {
    /**
     * 计算多个水库的库容曲线
     *
     * @param orgList code 主测站编码 waterLevel 测站水位
     * @return
     */
    @PostMapping("/reservoirCapacityCurve/calculationCapacity")
    CommonResult<Map<String, BigDecimal>> calculationCapacity(@RequestBody List<ReservoirCapacityCurve> orgList);
}
