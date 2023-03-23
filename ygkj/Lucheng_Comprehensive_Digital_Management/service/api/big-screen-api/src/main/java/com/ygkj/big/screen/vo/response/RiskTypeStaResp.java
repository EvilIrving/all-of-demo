package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 16:28
 */
@Data
@ApiModel("风险类型统计回参")
public class RiskTypeStaResp implements Serializable {

    @ApiModelProperty("数量")
    private Integer num;
    @ApiModelProperty("风险类型：251为隐患点，252为高风险点；253为薄弱点")
    private String sbSign;
    @ApiModelProperty("风险类型名称")
    private String sbSignName;
    @ApiModelProperty("比率")
    private String rate;
    @ApiModelProperty("总数")
    private Integer total;
}
