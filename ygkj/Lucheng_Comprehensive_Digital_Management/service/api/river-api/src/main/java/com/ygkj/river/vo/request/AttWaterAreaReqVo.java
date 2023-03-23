package com.ygkj.river.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 水域面积表
 * @date 2021-08-12
 */
@Data
@ApiModel("水域面积表")
@NoArgsConstructor
public class AttWaterAreaReqVo implements Serializable {

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
     * 水域类型 1:河道水域面 2:其他水域水域面 3:人工水道水域面 4:山塘水域面 5:水库水域面
     */
    @ApiModelProperty("水域类型 1:河道水域面 2:其他水域水域面 3:人工水道水域面 4:山塘水域面 5:水库水域面")
    private String type;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

}