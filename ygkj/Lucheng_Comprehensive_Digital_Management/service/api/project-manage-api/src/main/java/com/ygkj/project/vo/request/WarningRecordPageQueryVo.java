package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WarningRecordPageQueryVo {

    @ApiModelProperty("查询单条时用")
    private String id;
    @ApiModelProperty("起始时间 yyyy-mm-dd HH:mm:ss")
    private String start;
    @ApiModelProperty("结束时间 yyyy-mm-dd HH:mm:ss")
    private String end;
    @ApiModelProperty("摄像机名称")
    private String equipmentName;
    @ApiModelProperty("页码")
    private Integer page;
    @ApiModelProperty("每页记录条数")
    private Integer rows;
    @ApiModelProperty("前端忽略")
    private List<String> projectCodes;
    @ApiModelProperty("是否全部查询 前端忽略")
    private boolean fetchAll = false;

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
