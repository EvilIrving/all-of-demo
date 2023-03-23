package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("水库列表返回参数")
public class ReservoirProjectResp implements Serializable {

    @ApiModelProperty("水库名称")
    private String resName;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在县")
    private String countryAdnm;

    @ApiModelProperty("水库规模 1:大（Ⅰ）型 2:大（Ⅱ）型 3:中型 4:小（Ⅰ）型 5:小（Ⅱ）型")
    private String engScal;

    @ApiModelProperty("总库容(万方)")
    private String totCap;

    @ApiModelProperty("死库容(万方)")
    private String deadCap;

    @ApiModelProperty("经度")
    private String lowLeftLong;

    @ApiModelProperty("纬度")
    private String lowLeftLat;

    @ApiModelProperty("管理层级")
    private String managementLevel;


}
