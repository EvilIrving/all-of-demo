package com.ygkj.project.vo.response;

import com.ygkj.gragh.model.AttSeawallBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/10/10
 */
@Data
@ApiModel("海塘回参")
public class SeawallResVo extends AttSeawallBase {
    private static final long serialVersionUID = 1222538943804152933L;

    @ApiModelProperty("行政区划编码")
    private String areaName;

    @ApiModelProperty("报警次数")
    private int monitorWarnTimes = 0;

    @ApiModelProperty("监测潮位值")
    Double monitorTdz;

    @ApiModelProperty("监测潮位时刻")
    Date monitorTdzTime;

    @ApiModelProperty("监测报警级别：1：蓝色；2：黄色；3：橙色；4：红色；0，正常")
    int monitorWarnState = 0;

    @ApiModelProperty("监测报警时刻")
    Date monitorWarnTime;

    @ApiModelProperty("预报潮位值")
    Double forecastTdz;

    @ApiModelProperty("预报潮位时刻")
    Date forecastTdzTime;

    @ApiModelProperty("预报报警级别：1：蓝色；2：黄色；3：橙色；4：红色；0，正常")
    int forecastWarnState = 0;

    @ApiModelProperty("预报报警时刻")
    Date forecastWarnTime;
}
