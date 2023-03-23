package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class KpakmaBaseResVo {

    @ApiModelProperty("水土流失区编码")
    private String kpakmaCode;

    @ApiModelProperty("水土流失区名称")
    private String kpakmaName;

    @ApiModelProperty("水土流失程度")
    private BigDecimal kpakmaArea;

    @ApiModelProperty("水土流失程度")
    private Integer kpakmaDegree;

    @ApiModelProperty("城市名称")
    private String cityAdnm;

    @ApiModelProperty("区/县名称")
    private String countryAdnm;
}
