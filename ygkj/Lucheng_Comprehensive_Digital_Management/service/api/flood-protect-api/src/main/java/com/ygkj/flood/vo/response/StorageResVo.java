package com.ygkj.flood.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("可蓄水量/纳蓄能力出参")
public class StorageResVo {

    @ApiModelProperty("水库编码")
    private String resCode;

    @ApiModelProperty("水库名称")
    private String resName;

    @ApiModelProperty("工程等别")
    private Integer engGrad;

    @ApiModelProperty("可蓄水量/纳蓄能力")
    private Double amount;

    @ApiModelProperty(value = "总库容", hidden = true)
    private Double totCap;

    @ApiModelProperty(value = "集雨面积", hidden = true)
    private Double watShedArea;

    @ApiModelProperty(value = "测站编码", hidden = true)
    private String stCode;

    @ApiModelProperty(value = "水位", hidden = true)
    private Double rz;
}
