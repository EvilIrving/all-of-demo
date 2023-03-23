package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class AttStBase {

    @TableId(type = IdType.INPUT)
    @ApiModelProperty("水文监测站代码")
    private String stCode;

    @ApiModelProperty("水文监测站名称")
    private String stName;

    @ApiModelProperty("水文监测站类型（rr水库水位站，pp雨量站，dd闸坝水位站，tt潮位站，zg地下水位站，zq流量站，zz河道，ss墒情，pt泵站，es沉降.di位移）")
    private String stType;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("防洪高水位")
    private Double floodLevel;

    @ApiModelProperty("保证水位")
    private Double guaranteeLevel;

    @ApiModelProperty("警戒水位")
    private Double warningLevel;

    @ApiModelProperty("基面高程")
    private Double dtmElev;

    @ApiModelProperty("水文监测站经度")
    private Double stLong;

    @ApiModelProperty("水文监测站维度")
    private Double stLat;

    @ApiModelProperty("日常水位")
    private Double dailyLevel;

    @ApiModelProperty("正常水位")
    private Double normalLevel;

    @ApiModelProperty("非汛限水位")
    private Double nonLimitLevel;
}
