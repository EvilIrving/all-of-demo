package com.ygkj.supervision.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ProjectCountReqVo extends PageEntity {


    /**
     * 根据统计类型
     */
    @ApiModelProperty("根据类型进行统计  area-按照地区 matter-按照检查事项 project-按照项目")
    private String countByType;

    @ApiModelProperty("开始日期")
    private String start;

    @ApiModelProperty("结束日期")
    private String end;

    @ApiModelProperty("检查事项类型")
    private String matterType;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("行政区划名称")
    private String areaName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程编码")
    private String projectCode;

}
