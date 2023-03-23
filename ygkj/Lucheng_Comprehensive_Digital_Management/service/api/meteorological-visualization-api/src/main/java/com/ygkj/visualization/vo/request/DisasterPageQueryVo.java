package com.ygkj.visualization.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class DisasterPageQueryVo extends PageEntity {

    @ApiModelProperty("灾害名称")
    private String name;
    @ApiModelProperty("灾害年份")
    private Integer year;
}
