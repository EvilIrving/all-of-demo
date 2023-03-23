package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 水域面积表
 * @date 2021-08-12
 */
@Data
@ApiModel("水域面积表")
public class AttWaterAreaRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 水域面积
     */
    @ApiModelProperty("水域面积")
    private BigDecimal waterArea;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String areaName;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer year;

    /**
     * 类型 1:河道水域面 2:其他水域水域面 3:人工水道水域面 4:山塘水域面 5:水库水域面
     */
    @ApiModelProperty("水域类型 1:河道水域面 2:其他水域水域面 3:人工水道水域面 4:山塘水域面 5:水库水域面")
    private String type;

    /**
     * 占用面积（㎡）
     */
    @ApiModelProperty("占用面积（㎡）")
    private BigDecimal occupiedArea;

    /**
     * 补偿面积（㎡）
     */
    @ApiModelProperty("补偿面积（㎡）")
    private BigDecimal compensationArea;

    @ApiModelProperty("值类型 1：最大值 2：正常值 3：最小值")
    private String valueType;

    @ApiModelProperty("水面率")////水域面积除以温州市总面积
    private BigDecimal waterRate;
}