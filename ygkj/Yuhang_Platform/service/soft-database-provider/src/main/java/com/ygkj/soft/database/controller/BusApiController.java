package com.ygkj.soft.database.controller;

import com.ygkj.soft.database.api.BusApiApi;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.ApiCategory;
import com.ygkj.soft.database.api.model.BusApi;
import com.ygkj.soft.database.api.model.BusApiApply;
import com.ygkj.soft.database.api.model.BusApiParam;
import com.ygkj.soft.database.service.BusApiService;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;
import ygkj.com.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/busApi")
public class BusApiController implements BusApiApi {

    @Autowired
    private BusApiService busApiService;

    @Override
    @PostMapping("/queryApiByPage")
    public CommonResult queryApiByPage(@RequestBody BusApiQueryVo queryVO) throws Exception {
        EasyuiPager easyuiPager = new EasyuiPager();
        BeanUtils.copyProperties(queryVO,easyuiPager);
        Pager pager = busApiService.pageList(queryVO, easyuiPager);
        return CommonResult.success(pager);
    }

    @Override
    @PostMapping("/saveApiData")
    public CommonResult saveApiData(@RequestBody BusApiUpdateVo updateVo) throws Exception {
        if(StringUtils.isNullOrEmpty(updateVo.getId())){
            busApiService.insert(updateVo);
        }else{
            busApiService.update(updateVo);
        }

        return CommonResult.success("保存成功");
    }

    @Override
    @GetMapping("/loadApi/{apiId}")
    public CommonResult loadApi(@PathVariable("apiId") String apiId) throws Exception {
        BusApi load = busApiService.load(apiId);
        return CommonResult.success(load);
    }

    @Override
    @DeleteMapping("/delApi/{apiId}")
    public CommonResult delApi(@PathVariable("apiId") String apiId) throws Exception {
        busApiService.delete(apiId);
        return CommonResult.success("删除成功");
    }

    @Override
    @PostMapping(value = "/queryApiParams")
    public CommonResult queryApiParams(@RequestBody BusApiParamQueryVo busApiParamQueryVo) throws Exception {
        List<BusApiParam> busApiParams = busApiService.queryApiParams(busApiParamQueryVo.getApiId(), busApiParamQueryVo.getBusType());
        return CommonResult.success(busApiParams);
    }

    @Override
    @DeleteMapping("/delApiParam/{apiParamId}")
    public CommonResult delApiParam(@PathVariable("apiParamId") String apiParamId) throws Exception {
        busApiService.delApiParam(apiParamId);
        return CommonResult.success("删除成功");
    }

    @Override
    @PostMapping("/saveApiParams")
    public CommonResult saveApiParams(@RequestBody List<BusApiParamUpdateVo> paramsList) throws Exception {
        busApiService.saveApiParams(paramsList);
        return CommonResult.success("接口参数保存成功");
    }

    @Override
    @PostMapping("/apiCategoryTree")
    public CommonResult apiCategoryTree() throws Exception {
        List<ApiCategory> apiCategories = busApiService.apiCategoryTree();
        return CommonResult.success(apiCategories);
    }

    @Override
    @PostMapping("/saveApiCategory")
    public CommonResult saveApiCategory(@RequestBody ApiCategoryUpdateVo apiCategoryUpdateVo) throws Exception {
        if(StringUtils.isNullOrEmpty(apiCategoryUpdateVo.getId())){
            if(StringUtils.isNullOrEmpty(apiCategoryUpdateVo.getParentId())){
                apiCategoryUpdateVo.setParentId(null);
            }
            busApiService.saveApiCategory(apiCategoryUpdateVo);
        }else{
            busApiService.updateApiCategory(apiCategoryUpdateVo);
        }
        return CommonResult.success("接口分类保存成功");
    }

    @Override
    @DeleteMapping("/delApiCategory")
    public CommonResult delApiCategory(@PathVariable("apiCategoryId") String apiCategoryId) throws Exception {
        return null;
    }


    @PostMapping("/api/{outUrl}")
    public CommonResult api(HttpServletRequest request,@PathVariable("outUrl") String outUrl) throws Exception {
        return busApiService.externalCall(request,outUrl);
    }
}
