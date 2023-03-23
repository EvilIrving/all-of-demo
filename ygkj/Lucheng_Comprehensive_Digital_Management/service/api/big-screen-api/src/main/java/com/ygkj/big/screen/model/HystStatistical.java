package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("hyst_statistical")
public class HystStatistical implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 电站编码
     */
    @ApiModelProperty("电站编码")
    private String hystCode;

    /**
     * 监测点编号
     */
    @ApiModelProperty("监测点编号")
    private String pointCode;

    /**
     * 监测点类型
     */
    @ApiModelProperty("监测点类型")
    private String pointType;

    /**
     * 统计时间
     */
    @ApiModelProperty("统计时间")
    private Date staTime;

    /**
     * 统计时间内最大流量
     */
    @ApiModelProperty("统计时间内最大流量")
    private Float maxFlow;

    /**
     * 监测时间
     */
    @ApiModelProperty("监测时间")
    private Date recTime;

    /**
     * 最枯月90%平均值
     */
    @ApiModelProperty("最枯月90%平均值")
    private Float aprvEcoFlowMonth;

    /**
     * 多年平均流量10%
     */
    @ApiModelProperty("多年平均流量10%")
    private Float aprvEcoFlowYear;

    /**
     * 设备是否在线
     */
    @ApiModelProperty("设备是否在线")
    private String equipmentOnline;

    /**
     * 静态图像是否上传
     */
    @ApiModelProperty("静态图像是否上传")
    private String staPicUpload;

    /**
     * 监测数据是否上传
     */
    @ApiModelProperty("监测数据是否上传")
    private String flowUpload;

    /**
     * 免考
     */
    @ApiModelProperty("免考")
    private String ispass;

    /**
     * 免考对应的审批id
     */
    @ApiModelProperty("免考对应的审批id")
    private String passid;

    public HystStatistical() {
    }

}

