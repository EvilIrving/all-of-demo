package com.ygkj.flood.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class IntelligentAnalysisVo {

    @ApiModelProperty("队伍id")
    String teamId;
    @ApiModelProperty("物资列表")
    List<MaterialDispatchRequestVo> list;
}
