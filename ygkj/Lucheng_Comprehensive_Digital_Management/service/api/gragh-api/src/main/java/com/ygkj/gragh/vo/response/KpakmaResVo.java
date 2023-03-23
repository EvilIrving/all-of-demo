package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KpakmaResVo {

    @ApiModelProperty("水土流失区编码")
    private String kpakmaCode;

    @ApiModelProperty("水土流失区名称")
    private String kpakmaName;

    @ApiModelProperty("水土流失面积")
    private BigDecimal kpakmaAera;

    @ApiModelProperty("水土流失程度")
    private Integer kpakmaDegree;

//    @ApiModelProperty("级别")
//    private String llevel;

    @ApiModelProperty("范围")
    private String kpakmaRange;
}
