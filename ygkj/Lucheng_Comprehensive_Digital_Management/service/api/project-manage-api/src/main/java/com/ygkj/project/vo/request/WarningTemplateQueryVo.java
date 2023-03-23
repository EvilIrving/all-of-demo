package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarningTemplateQueryVo {

    @ApiModelProperty("id，查询单条记录")
    private String id;

    @ApiModelProperty("报警类型（摄像机/水位/雨量/台风/流量/蓄水量。。。）")
    private String warningType;
    @ApiModelProperty("每页记录数")
    private Integer rows = 10;
    @ApiModelProperty("当前页")
    private Integer page = 1;
    @ApiModelProperty("模板名称")
    private String templateName;
}
