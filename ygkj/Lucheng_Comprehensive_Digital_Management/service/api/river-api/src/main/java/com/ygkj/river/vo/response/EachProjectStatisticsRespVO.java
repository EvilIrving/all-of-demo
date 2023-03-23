package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("各个工程类型数量统计返回参数")
@Data
@NoArgsConstructor
public class EachProjectStatisticsRespVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("工程总数")
    private Integer projectTotalNum;

    @ApiModelProperty("项目在建(总)")
    private Integer underConstructionNum;

    @ApiModelProperty("已完工(总)")
    private Integer completeNum;
}
