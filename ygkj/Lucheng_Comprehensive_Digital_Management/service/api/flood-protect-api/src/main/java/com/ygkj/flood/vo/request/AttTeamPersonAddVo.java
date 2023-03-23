package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttTeamPersonAddVo {

    @ApiModelProperty("id")
    private String id;

    /**
     * 队伍人员姓名
     */
    @ApiModelProperty("队伍人员姓名")
    private String personName;

    /**
     * 办公电话
     */
    @ApiModelProperty("办公电话")
    private String officePhone;

    /**
     * 队伍人员联系方式
     */
    @ApiModelProperty("队伍人员联系方式")
    private String personPhone;

    /**
     * 队伍人员职务
     */
    @ApiModelProperty("队伍人员职务")
    private String personJob;

    /**
     * 队伍id
     */
    @ApiModelProperty("队伍id")
    @NotBlank(message = "队伍id不允许为空")
    private String teamId;
}
