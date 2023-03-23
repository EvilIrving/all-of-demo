package com.ygkj.digitization;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/9/7
 */
@Api(tags = "水文特征值接口")
public interface HydroFeatureControllerApi {
    @ApiOperation("查询水文特征表")
    CommonResult listFeatureTable(String tableNameKey);

    @ApiOperation("查询水文特征值")
    CommonResult loadFeatureValue(String tableName);
}
