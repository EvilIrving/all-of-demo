package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class LakeInfoPageQueryVo {

    @ApiModelProperty("每页限定记录数")
    private Integer pageSize;
    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("河道编码，查单条用")
    private String lakeCode;

    @ApiModelProperty("湖泊名称")
    private String lakeName;

    @ApiModelProperty("流域名称")
    private String basin;
}
