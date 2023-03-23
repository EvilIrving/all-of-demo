package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("水位曲线图出参")
public class WaterCurveResVo {

    @ApiModelProperty("当前水位")
    private BigDecimal currentLevel;

    @ApiModelProperty("警戒水位")
    private BigDecimal warningLevel;

    @ApiModelProperty("超汛限水位")
    private BigDecimal floodLevel;

    @ApiModelProperty("水位列表")
    private List<LevelResVo> list;
}
