package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class StormResVo implements Serializable {

    @ApiModelProperty("警报等级")
    private String result;

    @ApiModelProperty("路由id")
    private String urlId;
}
