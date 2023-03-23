package com.ygkj.soft.database.api;

import com.ygkj.soft.database.api.VO.BusinessDataQueryVO;
import com.ygkj.soft.database.api.VO.DataSourceDivideVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/9/16 13:39
 * @Description: 数源划分服务控制层抽象
 */
@Api(value = "DataSourceDivideApi", tags = "数源划分服务")
public interface DataSourceDivideApi {

    @ApiOperation("查询业务数据以及分配状态和科室")
    CommonResult businessDataWithDivideType(BusinessDataQueryVO businessDataQueryVO);

    @ApiOperation("划分数据到指定的科室")
    CommonResult divideBusinessData(DataSourceDivideVO divideVO);
}
