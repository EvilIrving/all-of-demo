package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("行政区划查询参数封装")
public class AdcdQueryVo {

    @ApiModelProperty("行政区划，可用于模糊查询，只查温州传3303即可")
    private String areaCode;

    @ApiModelProperty("等级，默认传3")
    private String level;

    @ApiModelProperty(value = "排序sql", hidden = true)
    private String orderSql;
}
