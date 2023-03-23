package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-10-18 15:32
 */
@Api(tags = "工情组件", value = "工情组件")
public interface WorkingConditionsControllerApi {

    @ApiOperation("水库、泵站、水闸工情统计")
    CommonResult workingConditionsTotal();

    @ApiOperation("水库、泵站、水闸工情列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="eng_scal",value ="工程规模" ),
            @ApiImplicitParam(name = "pjnm",value = "工程名称"),
            @ApiImplicitParam(name="pjtp",value = "工程类型"),
            @ApiImplicitParam(name="openStatus",value = "开闭标志"),
            @ApiImplicitParam(name="pageNum",value = "页数"),
            @ApiImplicitParam(name="pageSize",value = "页面大小")
    })
    CommonResult workingConditionsList(String code,String eng_scal, String pjnm,String pjtp,String openStatus,Integer pageNum,Integer pageSize);

    @ApiOperation("水库、泵站、水闸工情导出")
    void exportProject(HttpServletResponse httpServletResponse);

}
