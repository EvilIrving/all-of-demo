package com.ygkj.water.controller;

import com.ygkj.soft.database.api.BusApiApi;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.water.client.BusApiClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/busApi")
public class BusApiController implements BusApiApi {

    @Autowired
    private BusApiClient busApiClient;

    @Override
    @PostMapping("/queryApiByPage")
    public CommonResult queryApiByPage(BusApiQueryVo queryVO) throws Exception {
        return busApiClient.queryApiByPage(queryVO);
    }

    @Override
    @PostMapping("/saveApiData")
    public CommonResult saveApiData(BusApiUpdateVo updateVo) throws Exception {
        return busApiClient.saveApiData(updateVo);
    }

    @Override
    @GetMapping("/loadApi/{apiId}")
    public CommonResult loadApi(@PathVariable("apiId") String apiId) throws Exception {
        return busApiClient.loadApi(apiId);
    }

    @Override
    @DeleteMapping("/delApi/{apiId}")
    public CommonResult delApi(@PathVariable("apiId") String apiId) throws Exception {
        return busApiClient.delApi(apiId);
    }

    @Override
    @PostMapping("/queryApiParams")
    public CommonResult queryApiParams(BusApiParamQueryVo busApiParamQueryVo) throws Exception {
        return busApiClient.queryApiParams(busApiParamQueryVo);
    }

    @Override
    @DeleteMapping("/delApiParam/{apiParamId}")
    public CommonResult delApiParam(@PathVariable("apiParamId") String apiParamId) throws Exception {
        return busApiClient.delApiParam(apiParamId);
    }

    @Override
    @PostMapping("/saveApiParams")
    public CommonResult saveApiParams(@RequestBody List<BusApiParamUpdateVo> paramsList) throws Exception {
        return busApiClient.saveApiParams(paramsList);
    }

    @Override
    @PostMapping("/apiCategoryTree")
    public CommonResult apiCategoryTree() throws Exception {
        return busApiClient.apiCategoryTree();
    }

    @Override
    @PostMapping("/saveApiCategory")
    public CommonResult saveApiCategory(ApiCategoryUpdateVo apiCategoryUpdateVo) throws Exception {
        return null;
    }

    @Override
    public CommonResult delApiCategory(String apiCategoryId) throws Exception {
        return null;
    }
}
