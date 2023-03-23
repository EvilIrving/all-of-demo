package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.AttResBase;
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
@ApiModel("水库、测站、水位数据 查询 回参")
public class RsvrStationWaterResVo extends AttResBase implements Serializable {
    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    String areaName;

    /**
     * 实时水位对应库容
     */
    @ApiModelProperty("实时水位对应库容")
    private Double waterLevelCapacity;

    /**
     * 汛限水位对应库容
     */
    @ApiModelProperty("汛限水位对应库容")
    private Double limitLevelCapacity;

    /**
     * 防洪高水位对应库容
     */
    @ApiModelProperty("防洪高水位对应库容")
    private Double uppLevFlcoCapacity;

    /**
     * 正常水位对应库容
     */
    @ApiModelProperty("正常水位对应库容")
    private Double normWatLevCapacity;

    /**
     * 当前汛限水位
     * 大型水库 正常蓄水位/梅汛限水位（4-15~7-15）/台汛限水位（7-15~10-15）
     * 小型水库用于判断是否超汛的汛限水位（取溢洪道高程）
     */
    @ApiModelProperty("当前汛限水位")
    private BigDecimal currentLimitLevel;

    /**
     * 小型水库汛限水位(4-15 ~ 10-15 取主汛期防洪控制水位否则取正常蓄水水位)
     */
    @ApiModelProperty("当前汛限水位")
    private BigDecimal smallRsvrCurLimitLevel;

    /**
     * 超汛限水位 实时水位-汛限水位
     */
    @ApiModelProperty("超汛限水位")
    private Double overLimitVal;

    /**
     * 超防洪高水位
     */
    @ApiModelProperty("超防洪高水位")
    private Double overFloodVal;

    /**
     * 蓄水率
     */
    @ApiModelProperty("蓄水率")
    private Double storageRatio;

    /**
     * 至汛限纳蓄水能力
     */
    @ApiModelProperty("至汛限纳蓄能力")
    private Integer limitStorageAbility;

    /**
     * 至防洪高纳蓄水能力
     */
    @ApiModelProperty("至防洪高纳蓄能力")
    private Integer floodStorageAbility;

    /**
     * 至防洪高纳蓄水能力
     */
    @ApiModelProperty("至防洪高纳蓄能力")
    private Integer normStorageAbility;

    /**
     * 水位测量时间
     */
    @ApiModelProperty("水位测量时间")
    private Date dataTime;
    /**
     * 实时水位
     */
    @ApiModelProperty("实时水位")
    private Double waterLevel;

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
     * 水库泄流量
     */
    @ApiModelProperty("水库泄流量")
    private Double drainage;

    /**
     * 水库小时泄流量
     */
    @ApiModelProperty("水库24小时泄流量")
    private Double drainage24H;

    /**
     * 水库48小时泄流量
     */
    @ApiModelProperty("水库48小时泄流量")
    private Double drainage48H;

    /**
     * 水库72小时泄流量
     */
    @ApiModelProperty("水库72小时泄流量")
    private Double drainage72H;

    /**
     * 出库流量
     */
    @ApiModelProperty("出库流量")
    private String securityManageState;

    @ApiModelProperty("水库是否开启")
    private boolean rsvrOpened=false;
}
