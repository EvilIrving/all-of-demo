package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("水闸类型统计")
public class StatisticsWagaTypeResp implements Serializable {

    @ApiModelProperty("数量")
    private Integer num;
    @ApiModelProperty("类型 1:大（Ⅰ）型 2:大（Ⅱ）型 3:中型 4:小（Ⅰ）型 5:小（Ⅱ）型")
    private String engScal;
    @ApiModelProperty("类型名称")
    private String scalName;
}
