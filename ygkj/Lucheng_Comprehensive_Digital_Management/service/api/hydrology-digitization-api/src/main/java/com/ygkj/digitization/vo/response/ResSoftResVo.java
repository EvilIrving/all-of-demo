package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("水库安全分析出参")
public class ResSoftResVo {

    @ApiModelProperty("水库编码")
    private String resCode;

    @ApiModelProperty("水库名称")
    private String resName;

    @ApiModelProperty("行政区划编码")
    private String resLoc;

    @ApiModelProperty("行政区划名称")
    private String adnm;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("经度")
    private BigDecimal lowLeftLong;

    @ApiModelProperty("纬度")
    private BigDecimal lowLeftLat;

    @ApiModelProperty("关联测站编码")
    private String stCode;

    @ApiModelProperty("实时水位")
    private Double rz;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("总库容")
    private BigDecimal totCap;

    @ApiModelProperty("实时库容")
    private Double currentCap;

    @ApiModelProperty("集水面积")
    private BigDecimal watShedArea;

    @ApiModelProperty("纳蓄能力")
    private BigDecimal storageCap;
}
