package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 水源地基础信息表
 */
@Data
public class AttSwhsBase implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 水源地经度
     */
    @ApiModelProperty("水源地经度")
    private Double swhsLong;

    /**
     * 水源地纬度
     */
    @ApiModelProperty("水源地纬度")
    private Double swhsLat;

    /**
     * 水源地所在位置
     */
    @ApiModelProperty("水源地所在位置")
    private String swhsLoc;

    /**
     * 取水水源类型
     */
    @ApiModelProperty("取水水源类型")
    private String wainWasoType;

    /**
     * 水面面积
     */
    @ApiModelProperty("水面面积")
    private Double watArea;

    /**
     * 取水口数量
     */
    @ApiModelProperty("取水口数量")
    private Integer wainNum;

    /**
     * 主要取水用途
     */
    @ApiModelProperty("主要取水用途")
    private String mainWainUse;

    /**
     * 供水对象类型
     */
    @ApiModelProperty("供水对象类型")
    private String wasuObjType;

    /**
     * 主要供水城镇名称
     */
    @ApiModelProperty("主要供水城镇名称")
    private String wasuTownName;

    /**
     * 设计年取水量
     */
    @ApiModelProperty("设计年取水量")
    private Double desAnnWain;

    /**
     * 设计年供水人口
     */
    @ApiModelProperty("设计年供水人口")
    private Double desAnnWasuPop;

    /**
     * 投入运行年
     */
    @ApiModelProperty("投入运行年")
    private String runYear;

    /**
     * 水质目标
     */
    @ApiModelProperty("水质目标")
    private String waquGoal;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 记录生效时间
     */
    @ApiModelProperty("记录生效时间")
    private String effDate;

    /**
     * 记录失效时间
     */
    @ApiModelProperty("记录失效时间")
    private String exprDate;

    /**
     * 最大日供水能力
     */
    @ApiModelProperty("最大日供水能力")
    private String dateMaxWat;

    /**
     * 负责人
     */
    @ApiModelProperty("负责人")
    private String master;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String masterPhone;
}
