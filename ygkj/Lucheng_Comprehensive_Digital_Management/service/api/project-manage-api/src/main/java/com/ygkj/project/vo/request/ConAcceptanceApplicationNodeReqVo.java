package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConAcceptanceApplicationNodeReqVo {
    @ApiModelProperty("节点id")
    private Integer nodeId;

    @ApiModelProperty("单位id")
    private String unitId;


    @ApiModelProperty("审批意见")
    private String comments;


    @ApiModelProperty("合同申请id")
    private String applicationid;


    @ApiModelProperty("单位负责人id")
    private String unitReviewerId;

    @ApiModelProperty("节点状态，1-审批中 2-已驳回 3-通过")
    private String status;


    @ApiModelProperty("排序")
    private Integer sort;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;
}
