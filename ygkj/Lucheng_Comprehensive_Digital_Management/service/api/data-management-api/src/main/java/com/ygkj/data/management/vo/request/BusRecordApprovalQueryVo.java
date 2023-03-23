package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("审批记录查询参数封装")
public class BusRecordApprovalQueryVo extends PageEntity {
    @ApiModelProperty("查单条用")
    private Long id;

    @ApiModelProperty("前端忽略")
    private String tableId;
    @ApiModelProperty("前端忽略")
    private String batchId;
    @ApiModelProperty("前端忽略")
    private List<String> nextIds;
    @ApiModelProperty("审核状态：待审核、已驳回、已通过")
    private String status;
    @ApiModelProperty("检索内容（xxx名称）")
    private String recordPrimary;
    @ApiModelProperty("前端忽略")
    private String submitUserId;
    @ApiModelProperty("提交人")
    private String submitUserName;
    @ApiModelProperty("表名称")
    private String tableName;
    @ApiModelProperty("变动类型")
    private String operationType;
    @ApiModelProperty("表类型")
    private String category;
    @ApiModelProperty("时间排序，默认最近提交靠前，如果要最早的记录靠前，传false")
    private Boolean nearBy = Boolean.TRUE;
}
