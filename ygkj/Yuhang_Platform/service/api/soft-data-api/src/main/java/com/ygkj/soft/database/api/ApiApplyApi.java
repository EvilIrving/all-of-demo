package com.ygkj.soft.database.api;

import com.ygkj.soft.database.api.VO.ApiApplyQueryVo;
import com.ygkj.soft.database.api.VO.ApiApplyUpdateVo;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.validation.Valid;

/**
 *@描述 服务抽象
 *@参数
 *@返回值  
 *@创建人  wd
 *@创建时间  2020/9/17 21:27
 *@修改人和其它信息
 */
@Api(value = "接口(服务)审批管理",tags = "接口(服务)审批管理")
public interface ApiApplyApi {

    @ApiOperation(value = "待我审批列表及审批记录")
    CommonResult queryMyAuditTodo(ApiApplyQueryVo queryVO) throws Exception;

    @ApiOperation(value = "我发起的审批记录")
    CommonResult queryAuditRecordsFromMe(ApiApplyQueryVo queryVO) throws Exception;

    @ApiOperation(value = "查询审批详情")
    CommonResult loadApply(@ApiParam(value = "申请单ID",required = true) String applyId) throws Exception;

    @ApiOperation(value = "发起申请审批")
    CommonResult startApply(@Valid ApiApplyUpdateVo updateVo) throws Exception;

    @ApiOperation(value = "审批驳回")
    CommonResult reject(@ApiParam(value = "申请单ID",required = true) String applyId,
                        @ApiParam(value = "审批意见",required = true) String remark) throws Exception;

    @ApiOperation(value = "审批同意")
    CommonResult agree(@ApiParam(value = "申请单ID",required = true) String applyId) throws Exception;

    @ApiOperation(value = "删除审批")
    CommonResult delApply(@ApiParam(value = "申请单ID",required = true) String applyId) throws Exception;
}
