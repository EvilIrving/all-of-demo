package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: fml
 * @Date: 2020/9/8 19:41
 * @Description:
 */
@Data
public class BusProjectPageReqVo {

    @ApiModelProperty(value = "第几页")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize = 10;

    @ApiModelProperty(value = "行政区划")
    private String areaName;

    @ApiModelProperty(value = "项目类型")
    private String projectProperty;

    @ApiModelProperty(value = "申报时间")
    private String reportDate;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "项目单位")
    private String unitName;

    @ApiModelProperty(value = "自定义标签id(未设置传字符串-noId)")
    private String tagId;

}
