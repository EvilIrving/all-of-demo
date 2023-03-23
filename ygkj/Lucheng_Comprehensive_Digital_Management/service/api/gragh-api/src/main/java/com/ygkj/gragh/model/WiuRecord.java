package com.ygkj.gragh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WiuRecord {

    @ApiModelProperty("取水户编码")
    private String wiuCode;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("更新时间")
    private String updateTime;
}
