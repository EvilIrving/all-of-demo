package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("工程编辑入参")
public class ProjectEditReqVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("流域")
    private String bas;

    @ApiModelProperty("工程规模")
    private String engScal;

    @ApiModelProperty("工程等别")
    private String engGrad;

    @ApiModelProperty("政府负责人用户id")
    private String govermentChargeId;

    @ApiModelProperty("水行政部门负责人用户id")
    private String waterDepartmentChargeId;

    @ApiModelProperty("主管部门负责人id")
    private String competentDepartmentChargeId;

    @ApiModelProperty("管理单位责任人id")
    private String manageDepartmentChargeId;

    @ApiModelProperty("技术责任人")
    private String technologyChargeId;

    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;

    @ApiModelProperty("巡查责任人")
    private String points;
}
