package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("接口审批交互实体")
public class ApiApplyUpdateVo {

    /**
     * 接口id
     */
    @NotNull(message = "接口ID不允许为空！")
    @ApiModelProperty("接口id")
    private String apiId;

    /**
     * 联系人姓名
     */
    @NotNull(message = "联系人姓名不允许为空！")
    @ApiModelProperty("联系人姓名")
    private String contactMan;

    /**
     * 联系人电话
     */
    @NotNull(message = "联系人电话不允许为空！")
    @ApiModelProperty("联系人电话")
    private String contactPhone;

    /**
     * 联系人邮箱
     */
    @ApiModelProperty("联系人邮箱")
    private String contactMail;

    /**
     * 申请依据
     */
    @ApiModelProperty("申请依据")
    private String applyReason;

    /**
     * 系统名称
     */
    @NotNull(message = "系统名称不允许为空！")
    @ApiModelProperty("系统名称")
    private String applySystem;

}
