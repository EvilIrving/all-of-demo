package com.ygkj.project.vo.response;

import com.ygkj.gragh.model.AttWagaBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/10
 */
@Data
@ApiModel("水闸、测站、水位数据 查询 回参")
public class WagaStationWaterResVo extends AttWagaBase implements Serializable {
    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    String areaName;

    /**
     * 水位测量时间
     */
    @ApiModelProperty("水位测量时间")
    private Date dataTime;

    /**
     * 闸上水位
     */
    @ApiModelProperty("闸上水位")
    private Double upz;

    /**
     * 闸下水位
     */
    @ApiModelProperty("闸下水位")
    private Double dwz;
    /**
     * 最大过闸流量
     */
    @ApiModelProperty("最大过闸流量")
    private String mxgtq;
    /**
     * 开度
     */
    @ApiModelProperty("开度")
    private String ovs;

    /**
     * 流量
     */
    @ApiModelProperty("流量")
    private String ll;

    /**
     * 水文监测站代码
     */
    @ApiModelProperty("水文监测站代码")
    private String stCode;

    /**
     * 水文监测站名称
     */
    @ApiModelProperty("水文监测站名称")
    private String stName;

    /**
     * 水文监测站类型
     */
    @ApiModelProperty("水文监测站类型")
    private String stType;

    /**
     * 水文监测站经度
     */
    @ApiModelProperty("水文监测站经度")
    private BigDecimal stLong;

    /**
     * 水文监测站纬度
     */
    @ApiModelProperty("水文监测站纬度")
    private BigDecimal stLat;

    /**
     * 闸门开度阈值
     */
    @ApiModelProperty("闸门开度阈值")
    private Double gateOpenThreshold;

    /**
     * 闸门是否打开
     */
    @ApiModelProperty("闸门是否打开")
    private boolean gateOpened = false;

    /**
     * 排洪量
     */
    @ApiModelProperty("排洪量")
    private Double drainage;
}
