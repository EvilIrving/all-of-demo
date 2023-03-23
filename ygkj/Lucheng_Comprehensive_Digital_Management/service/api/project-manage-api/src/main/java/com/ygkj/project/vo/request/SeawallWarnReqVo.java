package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/10/10
 */
@Data
@ApiModel("潮位监测/预报查询入参")
@ToString
public class SeawallWarnReqVo {
    @ApiModelProperty("1：监测，2，预报")
    Integer warnType;

    @ApiModelProperty("强制使用实时查出来的潮位数据，默认为同步程序监测的数据")
    boolean forceNewestWarnState;

    @ApiModelProperty("数据时刻，如果为空，则为最新值")
    Date dataTime;
}
