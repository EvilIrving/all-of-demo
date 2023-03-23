package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FlowStaInfoRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("测站编码")
    private String stCode;

    @ApiModelProperty("测站名")
    private String stName;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String areaCode;

    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty("报警阈值信息id")
    private String id;

    @ApiModelProperty("报警条件")
    private String warnCondition;

    @ApiModelProperty("流量阈值m³/s")
    private Double flowThreshold;
}
