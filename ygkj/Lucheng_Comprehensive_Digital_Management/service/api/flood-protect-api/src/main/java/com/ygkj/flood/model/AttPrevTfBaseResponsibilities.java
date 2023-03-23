package com.ygkj.flood.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * @description 山洪防止对象责任人表,数据来源于省厅数据仓
 * @author
 * @date 2022-05-05
 */
@Data
@ApiModel("山洪防止对象责任人表,数据来源于省厅数据仓")
public class AttPrevTfBaseResponsibilities implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 行政区划名称
     */
    @ApiModelProperty("行政区划名称")
    private String areaName;

    /**
     * 行政区划编码
     */
    @ApiModelProperty("行政区划编码")
    private String areaCode;

    /**
     * 行政村
     */
    @ApiModelProperty("行政村")
    private String avi;

    /**
     * 自然村
     */
    @ApiModelProperty("自然村")
    private String nvi;

    /**
     * 责任人类型
     */
    @ApiModelProperty("责任人类型")
    private String chargePersonType;

    /**
     * 责任人级别（ 县级/乡级/村级）
     */
    @ApiModelProperty("责任人级别（ 县级/乡级/村级）")
    private String chargePersonLevel;

    /**
     * 责任人姓名（可能同时包含多个）
     */
    @ApiModelProperty("责任人姓名（可能同时包含多个）")
    private String chargePersonName;

    /**
     * 责任人联系方式（可能同时包含多个）
     */
    @ApiModelProperty("责任人联系方式（可能同时包含多个）")
    private String chargePersonPhone;

    /**
     * 责任人所在单位
     */
    @ApiModelProperty("责任人所在单位")
    private String department;

    /**
     * 责任人职务
     */
    @ApiModelProperty("责任人职务")
    private String job;

    /**
     * 责任人个数
     */
    @ApiModelProperty("责任人个数")
    private Integer chargePersonNumber;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private Integer delFlag;

    /**
     * 防止对象id
     */
    @ApiModelProperty("防止对象id")
    private String baseId;

    /**
     * 行政区划排序标记
     */
    @ApiModelProperty("行政区划排序标记")
    private Integer orderFlag;

    /**
     * 所属区县名称
     */
    @ApiModelProperty("所属区县名称")
    private String countyName;

    public AttPrevTfBaseResponsibilities() {
    }

}
