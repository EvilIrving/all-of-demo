package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 每日数据质量同步任务结果
 * @date 2021-03-29
 */
@Data
@ApiModel("每日数据质量同步任务结果")
public class BusDataQuality implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 责任人部门
     */
    @ApiModelProperty("责任人部门")
    private String department;

    /**
     * 责任人
     */
    @ApiModelProperty("责任人")
    private String responsibility;

    /**
     * 数据项
     */
    @ApiModelProperty("数据项")
    private Integer tableNumber;

    /**
     * 数据id，关联到bus_data_table表
     */
    @ApiModelProperty("数据id，关联到bus_data_table表")
    private String tableIds;

    /**
     * 不完整数量
     */
    @ApiModelProperty("不完整数量")
    private Integer incompleteNumber;

    /**
     * 未更新数量
     */
    @ApiModelProperty("未更新数量")
    private Integer notUpToDateNumber;

    /**
     * 低质量数量
     */
    @ApiModelProperty("低质量数量")
    private Integer lowQualityNumber;

    public BusDataQuality() {
    }

}

