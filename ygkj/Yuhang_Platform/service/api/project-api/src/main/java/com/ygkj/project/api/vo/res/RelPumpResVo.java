package com.ygkj.project.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelPumpResVo extends RelProjectResVo{

    @ApiModelProperty("装机流量（m³/s）")
    private String installedFlow;

    @ApiModelProperty("装机功率（kw）")
    private String motorPower;

}
