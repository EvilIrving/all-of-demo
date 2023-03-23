package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("关联工程点位的查询参数封装")
public class ProjectRangeQueryVo {
    @ApiModelProperty("工程类型:大中型水库/小型水库/泵站/水闸/海塘/堤防/山塘/水电站/农村供水工程/在建工程")
    private String type;
    @ApiModelProperty("中心点纬度")
    private double latitude;
    @ApiModelProperty("中心点经度")
    private double longitude;
    @ApiModelProperty("距离(1/2/3)，单位公里")
    private Integer distance;

}
