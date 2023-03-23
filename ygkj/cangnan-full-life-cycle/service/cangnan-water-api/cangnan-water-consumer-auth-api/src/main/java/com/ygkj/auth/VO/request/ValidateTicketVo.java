package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Huang.zh
 * @date 2020/11/4 10:35
 * @Description:
 */
@Data
public class ValidateTicketVo {

    @ApiModelProperty(value = "票据，第三方登录用")
    @NotBlank(message = "票据不能为空")
    private String ticket;
    @ApiModelProperty(value = "系统类型")
//    @NotBlank(message = "请指定系统类型")
    private String type;
}
