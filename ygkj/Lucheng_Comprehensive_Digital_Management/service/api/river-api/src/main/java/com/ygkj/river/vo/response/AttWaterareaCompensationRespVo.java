package com.ygkj.river.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author hucong
 * @description 水域占用补偿表
 * @date 2021-08-12
 */
@Data
@ApiModel("水域占用补偿表")
@NoArgsConstructor
public class AttWaterareaCompensationRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer year;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String areaName;

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

}
