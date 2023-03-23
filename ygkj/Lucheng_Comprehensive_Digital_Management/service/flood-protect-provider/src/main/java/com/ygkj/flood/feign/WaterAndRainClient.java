package com.ygkj.flood.feign;

import com.ygkj.flood.model.StBase;
import com.ygkj.flood.model.StPptnR;
import com.ygkj.flood.vo.request.SynRainRequestVo;
import com.ygkj.flood.vo.request.WaterLevelReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @author xq
 * @Description 水雨工情-雨情 消费者
 * @Date 2021/1/18
 */
@FeignClient(name = "graph")
public interface WaterAndRainClient {

//    @PostMapping("/waterAndRain/rainfall")
//    CommonResult<List<StPptnR>> rainfall(@RequestParam("minutes") Integer minutes,
//                                         @RequestParam("max") Double max,
//                                         @RequestParam("min") Double min);
//
//    @PostMapping("/waterAndRain/rainfall")
//    CommonResult<List<StPptnR>> rainfall(@RequestParam("minutes") Integer minutes,
//                                         @RequestParam("max") Double max,
//                                         @RequestParam("min") Double min,
//                                         @RequestParam("areaCode") String areaCode);
//
//    @PostMapping("/waterAndRain/rainfall")
//    CommonResult<List<StPptnR>> rainfall(
//            @RequestParam("stationName")String stationName,
//            @RequestParam("areaCode") String areaCode,
//            @RequestParam("minutes") Integer minutes,
//            @RequestParam("min") Double min,
//            @RequestParam("max") Double max,
//            @RequestParam("warning") boolean warning);

    @PostMapping("/waterAndRain/rainfall")
    CommonResult<List<StPptnR>> rainfall(@SpringQueryMap SynRainRequestVo synRainRequestVo);

}
