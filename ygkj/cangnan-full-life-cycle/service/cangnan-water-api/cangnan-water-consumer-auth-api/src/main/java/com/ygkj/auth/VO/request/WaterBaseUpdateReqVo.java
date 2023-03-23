package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: huangzh
 * @Date: 2020/9/14 19:01
 * @Description:
 */
@Data
public class WaterBaseUpdateReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("水域名称")
    private String waterName;

    @ApiModelProperty("行政区划")
    private String areaName;

    @ApiModelProperty("水域面积（单位：m²）")
    private Double waterArea;

    @ApiModelProperty("库容量（单位：m³）")
    private Double capacity;

    @ApiModelProperty("实际岸线（m）")
    private Double shoreline;

    @ApiModelProperty("治导线（m）")
    private Double ruleWire;

    @ApiModelProperty("管理范围线（m）")
    private Double scopeLine;

    @ApiModelProperty("河底高程（m）")
    private Double waterHeight;

    @ApiModelProperty("水域类型")
    private String waterType;

    @ApiModelProperty("水质")
    private String waterQuality;

    @ApiModelProperty("管理单位")
    private String managementUnit;

    @ApiModelProperty("开发率")
    private Double developRate;

    @ApiModelProperty("负责人")
    private String responsiblePerson;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("水体用途")
    private String waterUse;

    @ApiModelProperty("岸线信息")
    private String shorelineInfo;

    @ApiModelProperty("范围线点位集合)")
    private String linePoint;

    @ApiModelProperty("临水线点位集合")
    private String waterfrontLinePoint;

    @ApiModelProperty("水面线点位集合")
    private String surfaceLinePoint;

    @ApiModelProperty("工程规模")
    private String grade;
}
