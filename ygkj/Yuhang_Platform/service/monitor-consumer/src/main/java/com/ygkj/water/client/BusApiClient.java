package com.ygkj.water.client;


import com.ygkj.soft.database.api.VO.BusApiParamQueryVo;
import com.ygkj.soft.database.api.VO.BusApiParamUpdateVo;
import com.ygkj.soft.database.api.VO.BusApiQueryVo;
import com.ygkj.soft.database.api.VO.BusApiUpdateVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@FeignClient(contextId = "BusApi",name = "soft-database")
public interface BusApiClient {


    @PostMapping("/busApi/queryApiByPage")
    CommonResult queryApiByPage(BusApiQueryVo queryVO) throws Exception;

    @PostMapping("/busApi/saveApiData")
    CommonResult saveApiData(BusApiUpdateVo updateVo) throws Exception;

    @GetMapping("/busApi/loadApi/{apiId}")
    CommonResult loadApi(@PathVariable("apiId") String apiId) throws Exception;

    @DeleteMapping("/busApi/delApi/{apiId}")
    CommonResult delApi(@PathVariable("apiId") String apiId) throws Exception;

    @PostMapping("/busApi/queryApiParams")
    CommonResult queryApiParams(BusApiParamQueryVo busApiParamQueryVo) throws Exception;

    @DeleteMapping("/busApi/delApiParam/{apiParamId}")
    CommonResult delApiParam(@PathVariable("apiParamId") String apiParamId) throws Exception;

    @PostMapping("/busApi/saveApiParams")
    CommonResult saveApiParams(@RequestBody List<BusApiParamUpdateVo> paramsList) throws Exception;

    @PostMapping("/busApi/apiCategoryTree")
    CommonResult apiCategoryTree() throws Exception;

    @PostMapping("/busApi/api/{outUrl}")
    CommonResult api(HttpServletRequest request,@PathVariable("outUrl") String outUrl);
}
