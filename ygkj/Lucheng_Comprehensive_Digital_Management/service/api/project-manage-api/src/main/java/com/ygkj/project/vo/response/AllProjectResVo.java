package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工程公共")
public class AllProjectResVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("经度")
    private Double longitude;

    @ApiModelProperty("纬度")
    private Double latitude;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名称")
    private String adnm;

    @ApiModelProperty("流域")
    private String bas;

    @ApiModelProperty("工程规模")
    private String engScal;

    @ApiModelProperty("工程等别")
    private String engGrad;

    @ApiModelProperty("政府负责人用户id")
    private String govermentChargeId;

    @ApiModelProperty("是否安全 true是 false否")
    private Boolean isSafe;

    @ApiModelProperty("政府负责人名称")
    private String govermentChargeName;

    @ApiModelProperty("政府负责人电话")
    private String govermentChargePhone;

    @ApiModelProperty("水行政部门负责人用户id")
    private String waterDepartmentChargeId;

    @ApiModelProperty("水行政部门负责人名称")
    private String waterDepartmentChargeName;

    @ApiModelProperty("水行政部门负责人电话")
    private String waterDepartmentChargePhone;

    @ApiModelProperty("主管部门负责人id")
    private String competentDepartmentChargeId;

    @ApiModelProperty("主管部门负责人名称")
    private String competentDepartmentChargeName;

    @ApiModelProperty("主管部门负责人电话")
    private String competentDepartmentChargePhone;

    @ApiModelProperty("管理单位责任人id")
    private String manageDepartmentChargeId;

    @ApiModelProperty("管理单位责任人名称")
    private String manageDepartmentChargeName;

    @ApiModelProperty("管理单位责任人电话")
    private String manageDepartmentChargePhone;

    @ApiModelProperty("技术责任人")
    private String technologyChargeId;

    @ApiModelProperty("技术责任人名称")
    private String technologyChargeName;

    @ApiModelProperty("技术责任人电话")
    private String technologyChargePhone;

    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;

    @ApiModelProperty("巡查责任人名称")
    private String patrolChargeName;

    @ApiModelProperty("巡查责任人电话")
    private String patrolChargePhone;

    @ApiModelProperty("关联摄像机数量")
    private Integer cameraNum;

    @ApiModelProperty("线图层点位数组")
    private String points;
}
