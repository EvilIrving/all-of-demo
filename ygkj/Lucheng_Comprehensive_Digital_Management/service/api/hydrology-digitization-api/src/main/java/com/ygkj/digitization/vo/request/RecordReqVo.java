package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RecordReqVo {

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

    @ApiModelProperty("目录id")
    private String catalogId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "上传人id", hidden = true)
    private String uploaderId;

    @ApiModelProperty(value = "上传人姓名", hidden = true)
    private String uploaderName;

    @ApiModelProperty(value = "部门id", hidden = true)
    private String deptId;
}
