package com.ygkj.visualization.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 人口信息
 */
@Data
public class PopulationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("人口（万人）")
    private Double popNum;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty("行政区划名")
    private String countyName;

    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createId;

    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date updateTime;

    @ApiModelProperty(value = "修改人", hidden = true)
    private String updateId;

    @ApiModelProperty(value = "是否删除 0-未删除 1-已删除", hidden = true)
    private boolean delFlag;
}
