package com.ygkj.project.vo.response;

import com.ygkj.project.model.ProjectRisk;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/5/15
 */
@Data
@ApiModel("工程风险隐患记录")
public class ProjRiskResVo extends ProjectRisk {
    @ApiModelProperty("行政区划名称")
    String areaName;

    @ApiModelProperty("地级市名称")
    String cityName;
}
