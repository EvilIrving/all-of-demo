package com.ygkj.soft.database.api;

import com.ygkj.soft.database.api.VO.BusinessDataQueryVO;
import com.ygkj.soft.database.api.VO.SoftDataAddVO;
import com.ygkj.soft.database.api.VO.SoftDataQueryVO;
import com.ygkj.soft.database.api.VO.SoftDataUpdateVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/9/8 18:05
 * @Description: 业务数据服务控制层抽象
 */
@Api(value = "BusinessDataApi", tags = "业务数据服务")
public interface BusinessDataApi {

    @ApiOperation(value = "查询业务数据列表")
    CommonResult queryBusinessDataByPage(BusinessDataQueryVO queryVO);

    @ApiOperation(value = "修改业务数据，进入管理员审核阶段")
    CommonResult insertSoftData(SoftDataAddVO softDataAddVO);

    @ApiOperation(value = "查询处于审核流程中的中间库数据")
    CommonResult querySoftData(SoftDataQueryVO softDataQueryVO);

    @ApiOperation(value = "审核中间库数据")
    CommonResult updateSoftDataStatus(SoftDataUpdateVO softDataUpdateVO);

    @ApiOperation(value = "为指定的业务表生成查询服务接口")
    CommonResult generateApi(String dataSourceName, String tableName);
}
