package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-23 10:59
 * @description 四乱问题统计
 */
@Data
public class RpProblemStatsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty("四乱类型（1乱占、2乱采、3乱堆、4乱建）")
    private String proType;

    @ApiModelProperty("状态（1未处理，2待审核，3已处理，4逾期未处理）")
    private String status;

    @ApiModelProperty("数量")
    private Integer num;
}
