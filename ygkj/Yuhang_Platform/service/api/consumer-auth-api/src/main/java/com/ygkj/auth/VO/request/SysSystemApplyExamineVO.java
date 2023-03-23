package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/10/27 13:50
 * @Description:
 */
@Data
public class SysSystemApplyExamineVO {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "审核状态 0未审核 1通过 2驳回")
    @NotNull(message = "请指定要更新的状态")
    private Integer status;
}
