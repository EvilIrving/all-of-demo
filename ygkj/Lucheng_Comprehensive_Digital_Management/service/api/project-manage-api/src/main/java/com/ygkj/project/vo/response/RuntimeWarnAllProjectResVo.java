package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class RuntimeWarnAllProjectResVo extends RuntimeWarnResVo{

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("安全鉴定时间")
    private String safetyTime;

    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("水位/闸上水位")
    private Double waterLevel;

    @ApiModelProperty("水闸闸下水位")
    private Double dwz;

    @ApiModelProperty("最大过闸流量")
    private String mxgtq;

    @ApiModelProperty("闸门开度")
    private String ovs;

    @ApiModelProperty("堤防规模")
    private String dikeGrad;

    @ApiModelProperty("规划防洪(潮)标准［重现期］（年）")
    private String planFlSta;

    @ApiModelProperty("堤防长度")
    private Integer dikeLen;

    /**
     * 所在村名称
     */
    private String village;
}
