package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("档案详情出参")
public class RecordResVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("备注")
    private String remark;

    private String uploaderName;

    private String category;

    private String fileUrl;
}
