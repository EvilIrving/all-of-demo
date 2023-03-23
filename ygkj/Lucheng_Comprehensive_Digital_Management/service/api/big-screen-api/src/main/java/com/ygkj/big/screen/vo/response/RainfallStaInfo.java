package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-31 14:17
 * @description 雨量测站信息
 */
@Data
public class RainfallStaInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;
    @ApiModelProperty("测站名")
    private String stName;
    @ApiModelProperty("流域")
    private String bas;
    @ApiModelProperty("行政区划")
    private String areaCode;
    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty("雨量")
    private Double drp = 0.0d;

    @ApiModelProperty(value = "1正常，0不正常", hidden = true)
    private String status = "1";

}
