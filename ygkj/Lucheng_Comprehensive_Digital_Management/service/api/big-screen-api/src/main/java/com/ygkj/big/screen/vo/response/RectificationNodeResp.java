package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 19:54
 */
@Data
@ApiModel("整改节点")
public class RectificationNodeResp implements Serializable {

    @ApiModelProperty("节点名称")
    private String name;
    @ApiModelProperty("整改内容")
    private String content;
    @ApiModelProperty("计划完成时间")
    private String plannedCompleteDate;
    @ApiModelProperty("完成时间")
    private String completeDate;
    @ApiModelProperty("相关文件List")
    private List<FileDetail> fileList;
    @ApiModelProperty("相关图片List")
    private List<FileDetail> imageList;
}
