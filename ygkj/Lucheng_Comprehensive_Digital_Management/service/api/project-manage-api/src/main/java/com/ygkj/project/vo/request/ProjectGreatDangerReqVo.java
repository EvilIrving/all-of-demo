package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectGreatDangerReqVo extends PageEntity {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("开始时间")
    private Date endTime;
}
