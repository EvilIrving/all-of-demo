package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RuntimeWarnResVo {

    @ApiModelProperty("海塘编码")
    private String seawallCode;

    @ApiModelProperty("海塘名称")
    private String seawallName;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("安全鉴定 0未超期 1超期")
    private Integer safety;

    @ApiModelProperty("汛前检查 0未超期 1超期")
    private Integer flood = 1;

    @ApiModelProperty("年度检查 0未超期 1已超期")
    private Integer yearReport = 1;

    @ApiModelProperty("巡查信息 0正常 1异常")
    private Integer patrol = 0;

    @ApiModelProperty("工程隐患 0无隐患 1有隐患 2有已处理的隐患")
    private Integer danger = 0;

    @ApiModelProperty("报警内容")
    private String context;

    @ApiModelProperty("海塘堤防线")
    private String coorpot;

    @ApiModelProperty("实际防潮标准")
    private String preventTideStandard;

    @ApiModelProperty("海塘长度")
    private BigDecimal seawallLength;

    @ApiModelProperty("安全鉴定结论")
    private String safetyConclusion;
}
