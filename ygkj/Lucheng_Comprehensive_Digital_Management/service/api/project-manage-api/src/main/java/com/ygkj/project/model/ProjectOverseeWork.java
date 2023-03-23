package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectOverseeWork {

    @TableId(type = IdType.INPUT)
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("整改人id")
    private String workerId;

    @ApiModelProperty("整改人姓名")
    private String workerName;

    @ApiModelProperty("整改人手机号")
    private String workerPhone;

    @ApiModelProperty("督办id")
    private String overseeId;

    @ApiModelProperty("人员类型 1内部人员 2外部人员")
    private Integer type;

    @ApiModelProperty("水管理平台待办id")
    private String reqId;
}
