package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AllStormTideResVo {

    @ApiModelProperty("台风风暴潮")
    private StormResVo typhoon;

    @ApiModelProperty("温带风暴潮")
    private StormResVo temperate;
}
