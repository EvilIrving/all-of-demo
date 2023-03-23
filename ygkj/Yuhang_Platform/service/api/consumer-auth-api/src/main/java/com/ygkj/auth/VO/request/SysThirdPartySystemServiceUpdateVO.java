package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Huang.zh
 * @date 2020/10/27 10:44
 * @Description:
 */
@Data
public class SysThirdPartySystemServiceUpdateVO {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "系统名称")
    private String systemName;

    @ApiModelProperty(value = "系统跳转url")
    private String url;
}
