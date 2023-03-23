package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WarningConfigPageQueryVo {
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("每页记录条数")
    private Integer rows;
    @ApiModelProperty("查询单条记录用")
    private String id;
    @ApiModelProperty("摄像机名称")
    private String equipmentName;
    @ApiModelProperty("前端忽略")
    private List<String> projectCodes;

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String adcd;

//    @ApiModelProperty("工程名称")
//    private String projectName;

    @ApiModelProperty("工程等别")
    private String engGrad;

    @ApiModelProperty("工程规模")
    private String engScal;
}
