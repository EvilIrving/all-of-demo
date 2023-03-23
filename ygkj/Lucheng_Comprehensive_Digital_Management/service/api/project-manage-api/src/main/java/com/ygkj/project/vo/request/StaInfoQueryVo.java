package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaInfoQueryVo {

    @ApiModelProperty("测站类型站点类型(RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站)")
    private String stType;

    @ApiModelProperty("降雨历时")
    private String rainfallPeriod;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("阈值类型(水库：汛限水位、防洪高水位;闸坝和河道：警戒水位、保证水位;潮位：蓝色警戒水位、黄色警戒水位、橙色警戒水位、红色警戒水位)")
    private String thresholdType;

    @ApiModelProperty("报警条件")
    private String warnCondition;

    @ApiModelProperty("是否是雨量站 1是 0否")
    private String isRainStation;

}
