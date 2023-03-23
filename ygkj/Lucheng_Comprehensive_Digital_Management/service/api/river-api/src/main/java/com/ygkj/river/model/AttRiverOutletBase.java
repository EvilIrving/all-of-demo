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
 * @description 入河排污口基本信息表（温州市第三次水资源调查评价报告附表 11-2-1）
 * @date 2021-10-09
 */
@Data
@ApiModel("入河排污口基本信息表（温州市第三次水资源调查评价报告附表 11-2-1）")
public class AttRiverOutletBase implements Serializable {


    private static final long serialVersionUID = 3009814802220832466L;
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 入河排污口名称
     */
    @ApiModelProperty("入河排污口名称")
    private String outletName;

    /**
     * 所属排污单位名称
     */
    @ApiModelProperty("所属排污单位名称")
    private String outletUnit;

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
     * 水资源二级区
     */
    @ApiModelProperty("水资源二级区")
    private String wrzGdseNm;

    /**
     * 水资源三级区
     */
    @ApiModelProperty("水资源三级区")
    private String wrzGdthNm;

    /**
     * 地 市
     */
    @ApiModelProperty("地 市")
    private String city;

    /**
     * 区县
     */
    @ApiModelProperty("区县")
    private String county;

    /**
     * 河流湖库
     */
    @ApiModelProperty("河流湖库")
    private String riverLakeRsvr;

    /**
     * 排入水功能区-是否划定水功能区
     */
    @ApiModelProperty("排入水功能区-是否划定水功能区")
    private String isWaterFunctionalZone;

    /**
     * 排入水功能区-一级水功 能区名称
     */
    @ApiModelProperty("排入水功能区-一级水功 能区名称")
    private String wfzGdfiNm;

    /**
     * 排入水功能区-二级水功能区名称
     */
    @ApiModelProperty("排入水功能区-二级水功能区名称")
    private String wfzGdseNm;

    /**
     * 排入水功能区-重要水功能区索引码
     */
    @ApiModelProperty("排入水功能区-重要水功能区索引码")
    private String importantWfzCode;

    /**
     * 入河方式
     */
    @ApiModelProperty("入河方式")
    private String intoRiverType;

    /**
     * 污水主要来源
     */
    @ApiModelProperty("污水主要来源")
    private String sewageMainSource;

    /**
     * 污水排放方式
     */
    @ApiModelProperty("污水排放方式")
    private String outletGrad;

    /**
     * 入河排污口规模
     */
    @ApiModelProperty("入河排污口规模")
    private String outletType;

    /**
     * 是否开展监测
     */
    @ApiModelProperty("是否开展监测")
    private String isSupervised;

    /**
     * 是否已批准或登记
     */
    @ApiModelProperty("是否已批准或登记")
    private String isPermitOrRegister;

    /**
     * 废污水年排放量(万 t/a)
     */
    @ApiModelProperty("废污水年排放量(万 t/a)")
    private BigDecimal sewageYearOutlet;

    /**
     * 主要污染物年入河量-化学需氧量(t/a)
     */
    @ApiModelProperty("主要污染物年入河量-化学需氧量(t/a)")
    private BigDecimal mpyirCod;

    /**
     * 主要污染物年入河量-氨氮(t/a)
     */
    @ApiModelProperty("主要污染物年入河量-氨氮(t/a)")
    private BigDecimal mpyirNh3N;

    /**
     * 主要污染物年入河量-总氮(t/a)
     */
    @ApiModelProperty("主要污染物年入河量-总氮(t/a)")
    private BigDecimal mpyirTn;

    /**
     * 主要污染物年入河量-总磷(t/a)
     */
    @ApiModelProperty("主要污染物年入河量-总磷(t/a)")
    private BigDecimal mpyirTp;

    /**
     * 主要污染物年入河量-其他项目(t/a)
     */
    @ApiModelProperty("主要污染物年入河量-其他项目(t/a)")
    private BigDecimal mpyirOther;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 填报单位
     */
    @ApiModelProperty("填报单位")
    private String fillUnit;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

    public AttRiverOutletBase() {
    }

}