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
@ApiModel("隐患点销号/薄弱点销号情况统计回参")
public class RiskHiddenDangerStaticResp implements Serializable {

    @ApiModelProperty("数量")
    private Integer num;
    @ApiModelProperty("名称")
    private String reccondition;
    @ApiModelProperty("比率")
    private String rate;
    @ApiModelProperty("总数")
    private Integer total;
}
