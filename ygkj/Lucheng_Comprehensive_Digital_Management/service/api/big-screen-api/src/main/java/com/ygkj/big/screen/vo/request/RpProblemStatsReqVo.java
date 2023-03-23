package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fml
 * @date 2021-03-23 9:41
 * @description 四乱问题统计查询条件
 */
@Data
public class RpProblemStatsReqVo {

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("1行政区划，2四乱类型，3状态")
    private String type;

    /*@ApiModelProperty("四乱类型（1乱占、2乱采、3乱堆、4乱建）")
    private String proType;

    @ApiModelProperty("状态（1未处理，2待审核，3已处理，4逾期未处理）")
    private String status;*/
}
