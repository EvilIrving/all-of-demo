package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WarningForwordUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id，统一采用雪花算法策略")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "转发id")
    private String forwordId;
}
