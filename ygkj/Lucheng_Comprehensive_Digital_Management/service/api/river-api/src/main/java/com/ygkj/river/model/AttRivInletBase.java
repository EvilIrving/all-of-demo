package com.ygkj.river.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description att_riv_inlet_base
 * @date 2021-08-06
 */
@Data
@ApiModel("新版河道雨水口排污口基础数据标")
public class AttRivInletBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String expNo;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 标高
     */
    @ApiModelProperty("标高")
    private BigDecimal surfH;

    /**
     * 管点类型
     */
    @ApiModelProperty("管点类型")
    private String feature;

    /**
     * 市政设置类型
     */
    @ApiModelProperty("市政设置类型")
    private String subid;

    /**
     * 便宜
     */
    @ApiModelProperty("便宜")
    private String offset;

    /**
     * 道路名称
     */
    @ApiModelProperty("道路名称")
    private String road;

    /**
     * 管线类型
     */
    @ApiModelProperty("管线类型")
    private String pType;

    /**
     * 1:雨水口；2：排污口
     */
    @ApiModelProperty("1:雨水口；2：排污口")
    private boolean recordType;

    public AttRivInletBase() {
    }

}
