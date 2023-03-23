package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("三个责任人详情入参")
public class ThreeChargeReqVo {

    @ApiModelProperty("政府负责人用户id")
    private String govermentChargeId;

    @ApiModelProperty("水行政部门负责人用户id")
    private String waterDepartmentChargeId;

    @ApiModelProperty("主管部门负责人用户id")
    private String competentDepartmentChargeId;

    @ApiModelProperty("管理单位责任人id")
    private String manageDepartmentChargeId;

    @ApiModelProperty("技术责任人")
    private String technologyChargeId;

    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;
}
