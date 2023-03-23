package com.ygkj.soft.database.api;

import com.ygkj.soft.database.api.VO.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @描述 服务抽象
 * @参数
 * @返回值
 * @创建人 wd
 * @创建时间 2020/9/17 21:27
 * @修改人和其它信息
 */
@Api(value = "接口（服务）管理")
public interface BusApiApi {

    @ApiOperation(value = "查询接口列表 -- 【接口基础信息】")
    CommonResult queryApiByPage(BusApiQueryVo queryVO) throws Exception;

    @ApiOperation(value = "新增/更新接口 -- 【接口基础信息】")
    CommonResult saveApiData(BusApiUpdateVo updateVo) throws Exception;

    @ApiOperation(value = "查询接口详情 -- 【接口基础信息】")
    CommonResult loadApi(@ApiParam(value = "接口ID", required = true) String apiId) throws Exception;

    @ApiOperation(value = "删除接口 -- 【接口基础信息】")
    CommonResult delApi(@ApiParam(value = "接口ID", required = true) String apiId) throws Exception;

    @ApiOperation(value = "查询接口参数 -- 【接口参数】")
    CommonResult queryApiParams(BusApiParamQueryVo busApiParamQueryVo) throws Exception;

    @ApiOperation(value = "删除接口参数 -- 【接口参数】")
    CommonResult delApiParam(@ApiParam(value = "接口参数ID", required = true) String apiParamId) throws Exception;

    @ApiOperation(value = "保存接口参数 -- 【接口参数】")
    CommonResult saveApiParams(@ApiParam(value = "接口参数列表", required = true) List<BusApiParamUpdateVo> paramsList) throws Exception;

    @ApiOperation(value = "接口分类树 -- 【接口分类】")
    CommonResult apiCategoryTree() throws Exception;

    @ApiOperation(value = "新增/更新接口分类 -- 【接口分类】")
    CommonResult saveApiCategory(ApiCategoryUpdateVo apiCategoryUpdateVo) throws Exception;

    @ApiOperation(value = "删除接口分类 -- 【接口分类】")
    CommonResult delApiCategory(@ApiParam(value = "接口参数ID", required = true) String apiCategoryId) throws Exception;
}
