package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaterExpertReqVo {

    @ApiModelProperty("擅长方向 1防汛抢险专家 2防汛调度专家")
    private Integer goodAt;

    @ApiModelProperty("专家类型 1堤防海塘抢险 2水闸泵站抢险")
    private Integer type;

    @ApiModelProperty("专家级别 1省级专家 2市级专家 3县级专家")
    private Integer level;

    @ApiModelProperty("专家名称")
    private String name;

    @ApiModelProperty("页码（不分页不用传）")
    private Integer pageNum;

    @ApiModelProperty("每页展示条数（不分页不用传）")
    private Integer pageSize;
}
