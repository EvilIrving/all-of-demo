package com.ygkj.flood.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 河道水情表
 */
@Data
public class StRiverRYyyymm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("合并测站编码")
    private String mgstcd;

    @ApiModelProperty("测站编码")
    private String stcd;

    @ApiModelProperty("监测时间")
    private Date tm;

    @ApiModelProperty("水位")
    private Double z;

    @ApiModelProperty("tag")
    private boolean tag;

    @ApiModelProperty("上报时间")
    private Date uploadTime;
}
