package com.ygkj.flood.feign;

import com.ygkj.flood.model.ReservoirCapacityCurve;
import com.ygkj.flood.model.StBase;
import com.ygkj.flood.vo.request.WaterLevelReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(contextId = "common-service", name = "common-service")
public interface WaterLevelClient {

//    @PostMapping("/synRainAndWater/waterStations")
//    CommonResult<List<StBase>> waterStations(@RequestBody @Valid WaterLevelReqVo synWaterRequestVo);

    @PostMapping("/synRainAndWater/waterStations")
    CommonResult<List<StBase>> waterStations(@RequestParam("areaCode") String areaCode,
                                             @RequestParam("stationName") String stationName,
                                             @RequestParam("stationType") String stationType);

    @PostMapping("/reservoirCapacityCurve/calculationCapacity")
    CommonResult<Map<String, BigDecimal>> calculationCapacity(@RequestBody List<ReservoirCapacityCurve> orgList);
}
