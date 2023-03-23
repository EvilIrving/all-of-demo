package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BhLgTermiteCheck {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("工程编号")
    private String pid;

    @ApiModelProperty("检查报告")
    private String report;

    @ApiModelProperty("检查时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date tm;
}
