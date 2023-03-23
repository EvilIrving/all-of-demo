package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class WarningHandleVo {

    @NotBlank(message = "请指定一条告警记录！")
    private String id;
    @NotBlank(message = "请输入处理意见！")
    @ApiModelProperty("处理意见")
    private String opinion;
    @ApiModelProperty("附件id，多个用逗号隔开")
    private String attachmentIds;
    @ApiModelProperty("我的弹窗跳转，增加追踪记录用")
    private String traceId;
}
