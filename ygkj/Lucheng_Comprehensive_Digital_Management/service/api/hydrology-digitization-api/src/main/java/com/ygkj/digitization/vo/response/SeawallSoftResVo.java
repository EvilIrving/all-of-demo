package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("海塘安全分析出参")
public class SeawallSoftResVo {

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("海塘名称")
    private String seawallName;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划编码")
    private String areaCode;

    @ApiModelProperty("行政区划名称")
    private String adnm;

    @ApiModelProperty("点位集合")
    private String point;

    @ApiModelProperty("关联潮位站编码")
    private String stCode;

    @ApiModelProperty("关联潮位站名称")
    private String stName;

    @ApiModelProperty("起点坝顶高程")
    private BigDecimal startCrestAltitude;

    @ApiModelProperty("终点坝顶高程")
    private BigDecimal finalCrestAltitude;

    @ApiModelProperty("潮位")
    private BigDecimal tdz;

    @ApiModelProperty("与潮位高差")
    private BigDecimal difTide;

}
