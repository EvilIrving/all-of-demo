package com.ygkj.big.screen;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
@Api(tags = "演练相关接口")
public interface DrillControllerApi {

    @ApiOperation("开始准备演练数据")
    CommonResult startDoDrill(String caseId, String unitName);

    @ApiOperation("开始一项演练事务")
    CommonResult startDrillAffair(String caseId);

    @ApiOperation("关闭一项演练事务")
    CommonResult closeDrillAffair(String affairId);

    @ApiOperation("列出演练方案")
    CommonResult listDrillCase(String unitName, Integer year, String caseType);

}
