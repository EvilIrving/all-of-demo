package com.ygkj.big.screen.vo.response;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 15:38
 */
@Data
public class BusRiskListResp implements Serializable {

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projName;

    /**
     * 工程类别
     */
    @ApiModelProperty("工程类别")
    private String projType;

    @ApiModelProperty("风险类型：251为隐患点，252为高风险点；253为薄弱点")
    private String sbSign;

    @ApiModelProperty("隐患类型：防汛准备、工程安全、检测预警、其他")
    private String dangerType;

    /**
     * 整改情况
     */
    @ApiModelProperty("整改情况")
    private String recCondition;

    /**
     * 整改情况
     */
    @ApiModelProperty("行政区划")
    private String county;

    /**
     * 上报人
     */
    @ApiModelProperty("上报人")
    private String reportPerson;

    /**
     * 整改责任单位
     */
    @ApiModelProperty("整改责任单位")
    private String dutyUnit;

    /**
     * 发现时间
     */
    @JSONField(format = "yyyy-MM-dd")
    @ApiModelProperty("发现时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;

    /**
     * 钱塘江工程id
     */
    @ApiModelProperty("钱塘江工程id")
    private String sbId;
    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String lng;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String lat;

    /**
     * 整改完成时间
     */
    @ApiModelProperty("整改完成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date rectifyFinishTime;


}
