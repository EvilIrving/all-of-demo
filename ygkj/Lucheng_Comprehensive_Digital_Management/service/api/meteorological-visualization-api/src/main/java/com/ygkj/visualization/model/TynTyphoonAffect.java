package com.ygkj.visualization.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 台风影响信息
 */
@Data
public class TynTyphoonAffect implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("台风id")
    private String typhoonId;

    @ApiModelProperty("台风名")
    private String typhoonName;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("发生时间")
    private String occurTime;

    @ApiModelProperty("小区")
    private String community;

    @ApiModelProperty("人员损失")
    private String personLoss;

    @ApiModelProperty("物资损失")
    private String materialLoss;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名")
    private String adnm;

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
