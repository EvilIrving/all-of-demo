package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("灌区列表返回参数")
public class IrrProjectResp implements Serializable {

    @ApiModelProperty("灌区名称")
    private String irrName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在县")
    private String countryAdnm;

    @ApiModelProperty("管理层级")
    private String managementLevel;

    @ApiModelProperty("设计灌溉面积(万亩)")
    private String desIrrArea;

    @ApiModelProperty("有效灌溉面积(万亩)")
    private String effIrraArea;

    @ApiModelProperty("受益范围")
    private String benRan;

    @ApiModelProperty("水源类型")
    private String majWasoType;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

}
