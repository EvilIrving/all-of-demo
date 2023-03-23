package com.ygkj.big.screen.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 15:26
 */
@Data
public class BusRiskListReq extends PageEntity implements Serializable {

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

    /**
     * 责任地区行政编码
     */
    @ApiModelProperty("责任地区行政编码")
    private String adcdDuty;

    /**
     * 流域
     */
    @ApiModelProperty("流域")
    private String riverBasin;

    /**
     * 整改情况
     */
    @ApiModelProperty("整改情况")
    private String recCondition;

    /**
     * 发现渠道（xxjc为县级检查；zczc为工程自查；sjcc为市级抽查；sjdc为省级督查）
     */
    @ApiModelProperty("发现渠道（xxjc为县级检查；zczc为工程自查；sjcc为市级抽查；sjdc为省级督查）")
    private String property;


    @ApiModelProperty("开始时间 格式：yyyy-MM-dd")
    private String startTime;

    @ApiModelProperty("结束时间 格式：yyyy-MM-dd")
    private String endTime;

    /**
     * 发现时间
     */
    @ApiModelProperty("发现时间 格式: yyyy-MM-dd")
    private String reportTime;
}
