package com.ygkj.visualization.vo.response;

import com.ygkj.visualization.model.AntiTyphoonInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdcdBaseDataStatisticsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总人口")
    private Double totalPop;

    @ApiModelProperty("总经济")
    private Double totalGDP;

    @ApiModelProperty("人口区划统计")
    private List<AdcdStatisticsInfo> adcdStatisticsInfos;

    @ApiModelProperty("区县gdp年度统计")
    private List<AreaGdpStatistics> gpdYearStatistics;

    @ApiModelProperty("防台联系人")
    private List<AntiTyphoonInfo> antiTyphoonInfos;
}
