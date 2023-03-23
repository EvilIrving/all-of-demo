package com.ygkj.data.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 数据审核记录批次表
 * @date 2021-06-25
 */
@Data
@ApiModel("数据审核记录批次表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusRecordApproval implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 批次号，每一次新增或者更新都有一个独立的批次号
     */
    @ApiModelProperty("批次号，每一次新增或者更新都有一个独立的批次号")
    private String batchId;

    /**
     * 审批状态：待审批、已驳回、已通过
     */
    @ApiModelProperty("审批状态：待审批、已驳回、已通过")
    private String status;

    /**
     * 下一个审核人的id
     */
    @ApiModelProperty("下一个审核人的id")
    private String nextId;

    /**
     * 表id
     */
    @ApiModelProperty("表id")
    private String tableId;

    /**
     * 表名称，冗余字段避免过多join查询
     */
    @ApiModelProperty("表名称，冗余字段避免过多join查询")
    private String tableName;

    /**
     * 记录id，暂定主键值
     */
    @ApiModelProperty("记录id，暂定主键值")
    private String recordPrimary;

    /**
     * 操作类型：新增、修改、删除
     */
    @ApiModelProperty("操作类型：新增、修改、删除")
    private String operationType;

    /**
     * 提交人id
     */
    @ApiModelProperty("提交人id")
    private String submitUserId;

    /**
     * 提交人名称，冗余字段避免过多join查询
     */
    @ApiModelProperty("提交人名称，冗余字段避免过多join查询")
    private String submitUserName;

    /**
     * 提交时间
     */
    @ApiModelProperty("提交时间")
    private Date submitTime;

    private List<BusRecordApprovalDetail> columns;

    private List<BusRecordApprovalHistory> history;

    private String category;

    public BusRecordApproval() {
    }

}

