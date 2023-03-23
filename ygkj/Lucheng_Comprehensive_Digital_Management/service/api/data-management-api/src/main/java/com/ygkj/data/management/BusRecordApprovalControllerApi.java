package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.BusColumnHistoryQueryVo;
import com.ygkj.data.management.vo.request.BusRecordApprovalHistoryQueryVo;
import com.ygkj.data.management.vo.request.BusRecordApprovalQueryVo;
import com.ygkj.warning.model.SysSendMessageHistory;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@Api(tags = "数据审批", value = "数据审批")
@Validated
public interface BusRecordApprovalControllerApi {

    @ApiOperation("我提交的---列表查询和单条查询通用接口")
    CommonResult mySubmit(BusRecordApprovalQueryVo queryVo);

    @ApiOperation("我提交的---列表查询导出")
    void exportMySubmit(BusRecordApprovalQueryVo queryVo, HttpServletResponse response) throws IOException;

    @ApiOperation("我提交的---删除一条流程")
    CommonResult deleteMyApproval(@Valid @NotNull(message = "需要指定一条要删除的流程！") Integer id);

    @ApiOperation("我提交的---撤回一条流程")
    CommonResult revokeMyApproval(@Valid @NotNull(message = "需要指定一条要撤回的流程！") Integer id);

    @ApiOperation("我的审核---列表查询待审核的记录，单条也可用")
    CommonResult myAudit(BusRecordApprovalQueryVo queryVo);

    @ApiOperation("我的审核---列表查询已驳回和已通过的记录，单条也可用")
    CommonResult myPastAudit(BusRecordApprovalHistoryQueryVo queryVo);

    @ApiOperation("审核一个流程")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "batchId", value = "流程批次id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "status", value = "审批操作：已驳回、已通过", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "opinion", value = "审批意见", dataType = "string", paramType = "query", required = true),
    })
    CommonResult audit(@Valid @NotBlank(message = "需要指定审核批次") String batchId,
                       @Valid @NotBlank(message = "需要提供审核操作") String status,
                       @Valid @NotBlank(message = "需要输入审核意见") String opinion);

    @ApiOperation("字段历史")
    CommonResult columnHistory(BusColumnHistoryQueryVo busColumnHistoryQueryVo);

    @ApiOperation("数据行修改历史")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "表id", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "primary", value = "主键值", dataType = "string", paramType = "query"),
    })
    CommonResult rowHistory(@Valid @NotBlank(message = "需要指定一张表") String tableId,
                            @Valid @NotBlank(message = "需要指定一个主键") String primary);

    @ApiOperation("溯源管理---列表查询")
    CommonResult selectApprovals(BusRecordApprovalQueryVo queryVo);

    @ApiOperation("溯源管理---列表导出")
    void exportSelectApprovals(BusRecordApprovalQueryVo queryVo, HttpServletResponse response) throws Exception;

    @ApiOperation("处理消息")
    CommonResult handle(SysSendMessageHistory history);
}
