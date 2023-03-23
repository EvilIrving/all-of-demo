package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("hyst_statistical_day")
public class HystStatisticalDay implements Serializable {

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
     * 统计时间
     */
    @ApiModelProperty("统计时间")
    private Date staDate;

    /**
     * 达标时长（小时）
     */
    @ApiModelProperty("达标时长（小时）")
    private Integer standardHourCount;

    /**
     * 未达标时长（小时）
     */
    @ApiModelProperty("未达标时长（小时）")
    private Integer unstandardHourCount;

    /**
     * 设备在线时长（小时）
     */
    @ApiModelProperty("设备在线时长（小时）")
    private Integer equipmentOnlineCount;

    /**
     * 设备不在线（小时）
     */
    @ApiModelProperty("设备不在线（小时）")
    private Integer unequipmentOnlineCount;

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

    public HystStatisticalDay() {
    }

}