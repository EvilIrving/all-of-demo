package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("河道最近点")
public class NearestTheRiverRespVo implements Serializable {

    private static final long serialVersionUID = 6865497230715742307L;

    @ApiModelProperty("纬度")
    private Double latitude;
    @ApiModelProperty("经度")
    private Double longitude;
    @ApiModelProperty("详细地址")
    private String address;
    @ApiModelProperty("区/街道")
    private String town;
    @ApiModelProperty("距离")
    private BigDecimal distance;
}
