package com.ygkj.digitization.vo.response;

import com.ygkj.digitization.model.AttSfBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/7/31
 */
@Data
@ApiModel("断面流量回参")
public class SectFlowResVo extends AttSfBase {
    private static final long serialVersionUID = -6454860235862066552L;

    /**
     * 水文监测站经度
     */
    @ApiModelProperty("水文监测站经度")
    private BigDecimal stLong;

    /**
     * 水文监测站维度
     */
    @ApiModelProperty("水文监测站维度")
    private BigDecimal stLat;

    @ApiModelProperty("水位")
    Double waterLevel;

    @ApiModelProperty("流量")
    Double flow;

    @ApiModelProperty("时刻")
    Date time;

    @ApiModelProperty("0：正常；1：橙色报警，2：红色报警")
    Integer warnType = 0;
}
