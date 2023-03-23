package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.GenManAddUpdateVo;
import com.ygkj.digitization.vo.request.GenManUpperQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description 日常业务管理-日常管理
 * @Date 2021/2/1
 */
@Api(tags = "日常业务管理-日常管理")
public interface GmGeneralControllerApi {

    @ApiOperation("市级人员查看")
    CommonResult upperQuery(GenManUpperQueryVo queryVo);

    @ApiOperation("县级人员查看")
    CommonResult subQuery(GenManUpperQueryVo queryVo);

    @ApiOperation("县级新增")
    CommonResult subAddAndUpdate(GenManAddUpdateVo addUpdateVo);

    @ApiOperation("县级删除")
    CommonResult subDelete(String genManId);
}
