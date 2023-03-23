package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusDataResponsbilityResVo {

    private String id;

    /**
     * 责任人姓名
     */
    @ApiModelProperty("责任人姓名")
    private String responsible;

    /**
     * 责任人电话
     */
    @ApiModelProperty("责任人电话")
    private String responsiblePhone;

    /**
     * 责任人部门
     */
    @ApiModelProperty("责任人部门")
    private String department;

    /**
     * 责任人职位
     */
    @ApiModelProperty("责任人职位")
    private String job;

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划")
    private String areaName;

    @ApiModelProperty("主键的值")
    private String primaryValue;
}
