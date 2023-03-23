package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.AttHystBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AttHystBaseResVo extends AttHystBase {

    /**
     * 多年平均流量10%
     */
    @ApiModelProperty("多年平均流量10%")
    private Float aprvEcoFlowYear;

    @ApiModelProperty("达标率")
    private Double standardRate;

    @ApiModelProperty("在线率")
    private Double onlineRate;

}
