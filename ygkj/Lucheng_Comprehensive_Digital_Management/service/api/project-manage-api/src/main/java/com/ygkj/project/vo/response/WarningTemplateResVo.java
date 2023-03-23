package com.ygkj.project.vo.response;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarningTemplateResVo {

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("模板名称")
    private String templateName;
    @ApiModelProperty("模板内容")
    private String templateContent;
    @ApiModelProperty("模板类型")
    private String warningType;
}
