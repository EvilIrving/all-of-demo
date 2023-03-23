package com.ygkj.warning.vo.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("对外开放发送消息api入参封装")
public class OpenApiSendMessageRequestVo {

    @NotBlank(message = "请指定要发送的号码！")
    private String phone;
    @NotBlank(message = "不允许发送空消息文本！")
    private String content;
}
