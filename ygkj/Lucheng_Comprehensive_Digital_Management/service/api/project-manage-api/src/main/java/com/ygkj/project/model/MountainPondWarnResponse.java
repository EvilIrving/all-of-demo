package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MountainPondWarnResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 山塘编号
     */
    @ApiModelProperty("山塘编号")
    private String mnpdCode;

    /**
     * 山塘编码
     */
    @ApiModelProperty("山塘编码")
    private String origCode;

    /**
     * 山塘名称
     */
    @ApiModelProperty("山塘名称")
    private String mnpdName;

    /**
     * 山塘类别
     */
    @ApiModelProperty("山塘类别")
    private String mnpdTypeName;

    /**
     * 所在市
     */
    @ApiModelProperty("所在市")
    private String cityName;

    /**
     * 所在县
     */
    @ApiModelProperty("所在县")
    private String countyName;

    /**
     * 乡镇村
     */
    @ApiModelProperty("乡镇村")
    private String towVillageName;

    /**
     * 监管部门单位名称
     */
    @ApiModelProperty("监管部门单位名称")
    private String superUnitName;

    /**
     * 监管部门分管负责人姓名
     */
    @ApiModelProperty("监管部门分管负责人姓名")
    private String superChargerName;

    /**
     * 监管部门分管负责人职务
     */
    @ApiModelProperty("监管部门分管负责人职务")
    private String superChargerPosition;

    /**
     * 监管部门分管负责人手机号码
     */
    @ApiModelProperty("监管部门分管负责人手机号码")
    private String superChargerPhone;

    /**
     * 基层水利员姓名
     */
    @ApiModelProperty("基层水利员姓名")
    private String superBasicName;

    /**
     * 基层水利员手机号码
     */
    @ApiModelProperty("基层水利员手机号码")
    private String superBasicPhone;

    /**
     * 管理部门单位名称
     */
    @ApiModelProperty("管理部门单位名称")
    private String manageUnitName;

    /**
     * 管理部门负责人姓名
     */
    @ApiModelProperty("管理部门负责人姓名")
    private String manageChargerName;

    /**
     * 管理部门负责人职务
     */
    @ApiModelProperty("管理部门负责人职务")
    private String manageChargerPosition;

    /**
     * 管理部门负责人手机号
     */
    @ApiModelProperty("管理部门负责人手机号")
    private String manageChargerPhone;

    /**
     * 巡查员姓名
     */
    @ApiModelProperty("巡查员姓名")
    private String managePatrolName;

    /**
     * 巡查员手机号码
     */
    @ApiModelProperty("巡查员手机号码")
    private String managePatrolPhone;

    /**
     * 所有权人统一社会信用代码
     */
    @ApiModelProperty("所有权人统一社会信用代码")
    private String ownerCreditCode;

    /**
     * 行业监管部门
     */
    @ApiModelProperty("行业监管部门")
    private String supervisionDept;

    /**
     * 行业监管部门统一社会信用代码
     */
    @ApiModelProperty("行业监管部门统一社会信用代码")
    private String supervisionCreditCode;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 新增时间
     */
    @ApiModelProperty("新增时间")
    private Date insertTime;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String adcd;

    /**
     * 同步时间
     */
    @ApiModelProperty("同步时间")
    private Date tongTime;
}
