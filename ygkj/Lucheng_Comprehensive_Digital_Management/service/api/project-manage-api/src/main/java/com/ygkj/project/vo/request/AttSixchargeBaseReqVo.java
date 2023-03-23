package com.ygkj.project.vo.request;

import com.ygkj.project.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttSixchargeBaseReqVo extends PageEntity {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("工程名称")
    private String projectName;


    @ApiModelProperty("工程类型")
    private String type;


    @ApiModelProperty("工程规模  传1，2，3，4，5")
    private String projectScale;




}
