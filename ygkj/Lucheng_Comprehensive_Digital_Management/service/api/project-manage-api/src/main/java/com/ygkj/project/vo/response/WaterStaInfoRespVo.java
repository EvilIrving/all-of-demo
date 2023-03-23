package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WaterStaInfoRespVo implements Serializable {

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

    @ApiModelProperty("汛限水位")
    private String limitLevel;

    @ApiModelProperty("防洪高水位")
    private String floodLevel;

    @ApiModelProperty("保证水位")
    private String guaranteeLevel;

    @ApiModelProperty("警戒水位")
    private String warningLevel;

    @ApiModelProperty("蓝色警戒水位")
    private String blueLevel;

    @ApiModelProperty("黄色警戒水位")
    private String yellowLevel;

    @ApiModelProperty("橙色警戒水位")
    private String orangeLevel;

    @ApiModelProperty("红色警戒水位")
    private String redLevel;

    @ApiModelProperty("报警阈值信息id")
    private String id;

    @ApiModelProperty("阈值类型(水库：汛限水位、防洪高水位;闸坝和河道：警戒水位、保证水位;潮位：蓝色警戒水位、黄色警戒水位、橙色警戒水位、红色警戒水位)")
    private String thresholdType;

    @ApiModelProperty("测站类型")
    private String stType;
}
