package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 水质测站表
 */
@Data
public class AttCwsstationBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 测站名称
     */
    @ApiModelProperty("测站名称")
    private String stationName;

    /**
     * 测站编码
     */
    @ApiModelProperty("测站编码")
    private String stationCode;

    /**
     * 村编码
     */
    @ApiModelProperty("村编码")
    private String village;

    /**
     * 镇编码
     */
    @ApiModelProperty("镇编码")
    private String town;

    /**
     * 街道编码
     */
    @ApiModelProperty("街道编码")
    private String street;

    /**
     * 所处流域
     */
    @ApiModelProperty("所处流域")
    private String riverId;

    /**
     * 地址
     */
    @ApiModelProperty("地址")
    private String address;

    /**
     * 负责人电话
     */
    @ApiModelProperty("负责人电话")
    private String phone;

    /**
     * 负责人姓名
     */
    @ApiModelProperty("负责人姓名")
    private String administer;

    /**
     * 所属供水站编码
     */
    @ApiModelProperty("所属供水站编码")
    private String projectCode;

    /**
     * 所属单位
     */
    @ApiModelProperty("所属单位")
    private Integer orgId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private String latitude;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private String longitude;

    /**
     * 村名
     */
    @ApiModelProperty("村名")
    private String villageName;

    /**
     * 镇名
     */
    @ApiModelProperty("镇名")
    private String townName;

    /**
     * 街道名
     */
    @ApiModelProperty("街道名")
    private String streetName;

    /**
     * 河流名
     */
    @ApiModelProperty("河流名")
    private String riverName;

    /**
     * 是否上报定川（0.否1.是）
     */
    @ApiModelProperty("是否上报定川（0.否1.是）")
    private Integer isReport;

    /**
     * 上报定川对应的名称
     */
    @ApiModelProperty("上报定川对应的名称")
    private String reportName;
}
