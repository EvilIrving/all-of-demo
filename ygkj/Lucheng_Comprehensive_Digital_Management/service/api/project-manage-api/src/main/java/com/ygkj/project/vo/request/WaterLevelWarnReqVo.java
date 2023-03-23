package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaterLevelWarnReqVo extends PageEntity {

    @ApiModelProperty("工程类型 水库 水闸 堤防")
    private String projectType;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("是否展示未超警数据，默认为false展示")
    private Boolean warning = false;

}
