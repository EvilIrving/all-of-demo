package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class TwoDutyResVo {

    @ApiModelProperty("负责人")
    private DutyResVo leader;

    @ApiModelProperty("值班员")
    private List<DutyResVo> worker;
}
