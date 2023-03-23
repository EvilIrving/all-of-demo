package com.ygkj.supervision.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 */
@Data
@ApiModel
public class ProjectPageQueryVo extends PageEntity {

    @ApiModelProperty("行政区划")
    private String areaName;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("事项名称")
    private String matterName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程编码，查单条记录")
    private String projectCode;

    @ApiModelProperty("开始时间")
    private String beginDate;

    @ApiModelProperty("结束时间")
    private String endDate;

    @ApiModelProperty("工程类型")
    private String type;

    @ApiModelProperty("所在位置点纬度")
    private Double latitude;
    @ApiModelProperty("所在位置点经度")
    private Double longitude;
    @ApiModelProperty("距离(1/2/3)，单位公里")
    private Integer distance;


}
