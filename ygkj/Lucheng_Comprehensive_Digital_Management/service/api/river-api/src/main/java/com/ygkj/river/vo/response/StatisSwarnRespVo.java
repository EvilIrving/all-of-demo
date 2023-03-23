package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("水源地C位统计")
@AllArgsConstructor
@NoArgsConstructor
public class StatisSwarnRespVo implements Serializable {

    @ApiModelProperty("可供水量")
    private BigDecimal total;
    @ApiModelProperty("暂无风险数量")
    private Integer noNum;
    @ApiModelProperty("红色预警数量")
    private Integer redNum;
    @ApiModelProperty("橙色预警数量")
    private Integer orangeNum;
    @ApiModelProperty("黄色预警数量")
    private Integer yellowNum;
    @ApiModelProperty("水源地总数量")
    private Integer riskTotalNum;

}
