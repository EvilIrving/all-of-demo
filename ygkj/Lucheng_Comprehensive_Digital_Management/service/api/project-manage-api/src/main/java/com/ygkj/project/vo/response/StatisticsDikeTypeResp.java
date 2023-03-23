package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("提防类型统计")
public class StatisticsDikeTypeResp implements Serializable {

    @ApiModelProperty("数量")
    private Integer num;
    @ApiModelProperty("类型 1:1级 2:2级 3:3级 4:4级 5:5级")
    private String engGrad;
    @ApiModelProperty("类型名称")
    private String scalName;
}
