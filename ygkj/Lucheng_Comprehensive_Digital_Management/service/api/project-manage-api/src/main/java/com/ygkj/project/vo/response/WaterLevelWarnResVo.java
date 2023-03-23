package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class WaterLevelWarnResVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("水位/闸上水位")
    private Double waterLevel;

    @ApiModelProperty("水闸闸下水位")
    private Double dwz;

    @ApiModelProperty("最大过闸流量")
    private String mxgtq;

    @ApiModelProperty("闸门开度")
    private String ovs;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("控运水位")
    private Double controlTransportLevel;

    @ApiModelProperty("测站编码")
    private String stcd;

    @ApiModelProperty("测站类型 RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站")
    private String sttp;

    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    @ApiModelProperty("是否报警 false没有报警 true报警")
    private boolean warning;

    @ApiModelProperty("水库安全鉴定结论")
    private String safetyConclusion;

    @ApiModelProperty("堤防规模")
    private String dikeGrad;

    @ApiModelProperty("规划防洪(潮)标准［重现期］（年）")
    private String planFlSta;

    @ApiModelProperty("堤防长度")
    private Integer dikeLen;

}
