package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 审批历史记录表
 * @date 2021-06-25
 */
@Data
@ApiModel("审批历史记录表")
public class BusRecordApprovalHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 批次id
     */
    @ApiModelProperty("批次id")
    private String batchId;

    /**
     * 节点审批用户id
     */
    @ApiModelProperty("节点审批用户id")
    private String userId;

    /**
     * 节点审批用户名称
     */
    @ApiModelProperty("节点审批用户名称")
    private String username;

    /**
     * 审核状态：驳回、通过
     */
    @ApiModelProperty("审核状态：驳回、通过")
    private String status;

    /**
     * 审核时间
     */
    @ApiModelProperty("审核时间")
    private Date operationTime;

    /**
     * 审核意见
     */
    @ApiModelProperty("审核意见")
    private String opinion;

    public BusRecordApprovalHistory() {
    }

    public BusRecordApprovalHistory(String batchId, String userId, String username, String status, Date operationTime, String opinion) {
        this.batchId = batchId;
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.operationTime = operationTime;
        this.opinion = opinion;
    }
}

