package com.ygkj.gragh.vo.request;


import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("河道查询参数封装")
public class RiverQueryVo extends PageEntity {

    @ApiModelProperty("河道名称")
    private String riverName;

    @ApiModelProperty("河道行政级别（市级/县级）")
    private String policy;

    @ApiModelProperty("行政区划代码")
    private String areaCode;

    @ApiModelProperty("水域年份，用于查询每一年地图上要展示的河道水域")
    private String year;

    @ApiModelProperty("河道id，查询单条记录详情用")
    private String id;
}
