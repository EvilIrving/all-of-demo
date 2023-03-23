package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Huang.zh
 * @date 2020/10/27 11:47
 * @Description:
 */
@Data
public class SysSystemApplyVO {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "请指定申请的用户")
    private String userId;

    @ApiModelProperty(value = "系统id")
    @NotBlank(message = "请指定申请的系统")
    private String systemId;


}
