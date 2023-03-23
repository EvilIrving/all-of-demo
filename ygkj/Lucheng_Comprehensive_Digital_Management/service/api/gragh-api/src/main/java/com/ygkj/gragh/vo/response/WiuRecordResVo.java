package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WiuRecordResVo {

    @ApiModelProperty("取水户编码")
    private String wiuCode;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;
}
