package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 取水户地图点位信息
 */
@Data
public class WuWaterUserMapRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("取水户编码")
    private String wiuCode;

    @ApiModelProperty("取水户名称")
    private String wiuName;

    @ApiModelProperty("经度")
    private Double wiuLong;

    @ApiModelProperty("纬度")
    private Double wiuLat;

    @ApiModelProperty("当年取水量")
    private Double wiuUse;

    @ApiModelProperty("许可取水量")
    private Double wpermitsize;

    @ApiModelProperty("状态 1正常、2许可证过期、3水量超许可、4掉线或未安装监控设施、5取水偏快提醒")
    private Integer status;
}
