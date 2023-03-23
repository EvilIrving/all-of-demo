package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RiverInfoPageQueryVo {

    @ApiModelProperty("id,查询单条用")
    private String id;

    @ApiModelProperty("河流名称")
    private String name;

    @ApiModelProperty("河流等级")
    private String riverGrade;

    @ApiModelProperty("河流类型")
    private String riverType;

    @ApiModelProperty("流域")
    private String basin;

    @ApiModelProperty("河道行政级别（市级/县级/乡级/省级）")
    private String policy;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页限定记录数")
    private Integer pageSize;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

}
