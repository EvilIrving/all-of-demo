package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaterExpertResVo {

    @ApiModelProperty("id")
    private String dtsCmfId;

    @ApiModelProperty("专家名称")
    private String name;

    @ApiModelProperty("所在单位")
    private String department;

    @ApiModelProperty("职务/职称")
    private String title;

    @ApiModelProperty("专家类型 1堤防海塘抢险 2水闸泵站抢险")
    private Integer type;

    @ApiModelProperty("擅长方向 1防汛抢险专家 2防汛调度专家")
    private Integer goodAt;

    @ApiModelProperty("联系方式")
    private String mobile;

    @ApiModelProperty("专家级别 1省级专家 2市级专家 3县级专家")
    private Integer level;
}
