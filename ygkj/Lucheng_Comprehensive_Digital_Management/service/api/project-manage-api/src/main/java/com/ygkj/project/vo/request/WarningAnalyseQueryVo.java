package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WarningAnalyseQueryVo {

    @ApiModelProperty("页码")
    private Integer page = 1;
    @ApiModelProperty("每页记录条数")
    private Integer rows = 10;
    @ApiModelProperty("是否查询全部")
    private boolean fetchAll;

    @ApiModelProperty("报警类型(1降雨，2水位，3流量，4蓄水量)")
    private String warnType;

    @ApiModelProperty("报警类型(前端不用管)")
    private String warningType;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("报警开始时间")
    private String startTime;

    @ApiModelProperty("报警结束时间")
    private String endTime;

    @ApiModelProperty("关键字(站点名称)")
    private String stName;

    @ApiModelProperty("站点类型(RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站)")
    private String staType;

    @ApiModelProperty("降雨历时(10min/30min/1h/3h/6h/24h)")
    private String rainfallPeriod;

    @ApiModelProperty("阈值类型(汛限水位/防洪高水位)")
    private String thresholdType;

    @ApiModelProperty("报警条件")
    private String warnCondition;

    @ApiModelProperty("测站id(单查)")
    private String stCode;

    @ApiModelProperty("报警配置id(单查)")
    private String configId;

    @ApiModelProperty("报警记录id(单查)")
    private String recordId;
}
