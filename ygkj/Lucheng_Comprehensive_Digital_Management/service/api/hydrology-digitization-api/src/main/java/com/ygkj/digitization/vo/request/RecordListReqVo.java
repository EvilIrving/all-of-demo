package com.ygkj.digitization.vo.request;

import com.ygkj.digitization.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RecordListReqVo extends PageEntity {

    @ApiModelProperty(value = "目录id", required = true)
    private String catalogId;

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty(value = "起始时间", hidden = true)
    private String startTime;

    @ApiModelProperty(value = "结束时间", hidden = true)
    private String endTime;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件名称", hidden = true)
    private List<String> catalogList;

}
