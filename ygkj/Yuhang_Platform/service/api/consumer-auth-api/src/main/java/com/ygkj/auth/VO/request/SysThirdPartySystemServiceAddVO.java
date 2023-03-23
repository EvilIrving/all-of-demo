package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Huang.zh
 * @date 2020/10/27 10:38
 * @Description:
 */

@Data
public class SysThirdPartySystemServiceAddVO {

    @ApiModelProperty(value = "系统名称")
    @NotBlank(message = "系统名称不能为空")
    private String systemName;

    @ApiModelProperty(value = "系统跳转url")
    @NotBlank(message = "url不能为空")
    private String url;
}
