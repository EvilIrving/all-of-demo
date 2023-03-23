package com.ygkj.project.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelGatestationResVo extends RelProjectResVo{

    @ApiModelProperty("装机功率（kw）")
    private String maxFlow;

    @ApiModelProperty("过闸流量（m³/s）")
    private String motorPower;
}
