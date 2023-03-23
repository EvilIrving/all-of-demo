package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/10
 */
@Data
@ApiModel("水库、测站、水位数据 查询入参")
public class RsvrStationWaterReqVo {

    @ApiModelProperty("水库名称")
    String resName;

    @ApiModelProperty("复盘时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    @ApiModelProperty("工程行政区划编码")
    String resAdcd;

    @ApiModelProperty("流域：飞云江、瓯江、鳌江")
    String resBas;

    @ApiModelProperty("水库工程等级：大（Ⅰ）型、大（Ⅱ）型、中型、小（Ⅰ）型、小（Ⅱ）型、大中型、小型")
    String engScales;

    @ApiModelProperty("是否计算实时库容")
    boolean waterLevelStorage = false;

    @ApiModelProperty("是否计算蓄水率")
    boolean storageRatio = false;

    @ApiModelProperty("是否计算纳蓄能力")
    boolean storageAbility = false;

    @ApiModelProperty("是否计算至汛限纳蓄水能力")
    boolean limitStorageAbility = false;

    @ApiModelProperty("是否计算至防洪高纳蓄水能力")
    boolean floodStorageAbility = false;

    @ApiModelProperty("是否计算至正常水位纳蓄水能力")
    boolean normStorageAbility = false;

    @ApiModelProperty("是否计算水库泄流量")
    boolean drainage = false;

    @ApiModelProperty("泄流时长")
    Integer drainageMinutes;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    /**
     * 水库小时泄流量
     */
    @ApiModelProperty("水库24小时泄流量")
    private Boolean drainage24H;

    /**
     * 水库48小时泄流量
     */
    @ApiModelProperty("水库48小时泄流量")
    private Boolean drainage48H;

    /**
     * 水库72小时泄流量
     */
    @ApiModelProperty("水库72小时泄流量")
    private Boolean drainage72H;
}
