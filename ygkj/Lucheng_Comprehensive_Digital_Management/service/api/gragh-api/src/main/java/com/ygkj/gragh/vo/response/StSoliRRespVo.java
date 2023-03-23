package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("墒情回参")
public class StSoliRRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * mgstcd
     */
    @ApiModelProperty("测站编码")
    private String mgstcd;

    /**
     * tm
     */
    @ApiModelProperty("时间")
    private Date tm;

    /**
     * slm10
     */
    @ApiModelProperty("10分钟数据")
    private Double slm10;

    /**
     * slm20
     */
    @ApiModelProperty("20分钟数据")
    private Double slm20;

    /**
     * slm30
     */
    @ApiModelProperty("30分钟数据")
    private Double slm30;

    public StSoliRRespVo() {
    }
}
