package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("档案列表出参")
public class RecordListResVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("上传人姓名")
    private String uploaderName;

    @ApiModelProperty("创建时间")
    private String createTime;

    private Boolean collected = Boolean.FALSE;
}
