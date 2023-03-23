package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author hucong
 * @description 水域占用补偿表
 * @date 2021-08-12
 */
@Data
@ApiModel("水域占用补偿表")
@NoArgsConstructor
public class AttWaterareaCompensationReqVo implements Serializable {

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

}
