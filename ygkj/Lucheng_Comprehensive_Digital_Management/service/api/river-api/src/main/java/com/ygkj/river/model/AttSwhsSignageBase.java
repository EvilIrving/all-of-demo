package com.ygkj.river.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description 水源地标识牌基本信息表
 * @author xq
 * @date 2021-11-17
 */
@Data
@ApiModel("水源地标识牌基本信息表")
public class AttSwhsSignageBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 标识牌名称
     */
    @ApiModelProperty("标识牌名称")
    private String signageName;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    /**
     * 图片url
     */
    @ApiModelProperty("图片url")
    private String imgUrls;

    /**
     * 水源地代码
     */
    @ApiModelProperty("水源地代码")
    private String swhsCode;

    /**
     * 水源地名称
     */
    @ApiModelProperty("水源地名称")
    private String swhsName;

    public AttSwhsSignageBase() {
    }

}


