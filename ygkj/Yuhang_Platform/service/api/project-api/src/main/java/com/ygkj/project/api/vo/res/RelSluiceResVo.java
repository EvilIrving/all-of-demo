package com.ygkj.project.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelSluiceResVo extends RelProjectResVo {

    @ApiModelProperty("过闸流量(m³/s)")
    private String maxFlow;
}
