package com.ygkj.gragh.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WiuPlan {

    private static final long serialVersionUID = 1L;

    /**
     * 取水户编码
     */
    @ApiModelProperty("取水户编码")
    private String wiuCode;

    /**
     * 文件id
     */
    @ApiModelProperty("文件id")
    private String fileId;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createTime;
}
