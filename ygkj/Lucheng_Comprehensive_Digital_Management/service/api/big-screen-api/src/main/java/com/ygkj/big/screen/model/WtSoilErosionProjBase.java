package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-30 15:39
 * @description 生产建设项目-基本信息
 */
@Data
@ApiModel("生产建设项目-基本信息")
public class WtSoilErosionProjBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，统一采用雪花算法")
    private String id;

    @ApiModelProperty("项目名称")
    private String projName;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("行政区划名")
    private String adnm;

    @ApiModelProperty("涉及乡镇")
    private String involveTown;

    @ApiModelProperty("涉及自然村组")
    private String involveVillage;

    @ApiModelProperty("治理面积")
    private Double governArea;

    @ApiModelProperty("实施年度")
    private String implementYear;

    @ApiModelProperty("实施方案报告id")
    private String reportFileId;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("审查时间")
    private String checkTime;

    @ApiModelProperty("是否需要招投标(0否，1是)")
    private String bid;

    @ApiModelProperty("批复单位")
    private String approvalUnit;

    @ApiModelProperty("土地总面积k㎡")
    private Double landArea;

    @ApiModelProperty("水土流失面积k㎡")
    private Double soilErosionArea;

    @ApiModelProperty("年土壤侵蚀量 万t")
    private Double erosion;

    @ApiModelProperty("土壤侵蚀模数(t/k㎡)")
    private Double erosionMo;

    @ApiModelProperty("图片ids，多个以逗号分隔")
    private String imgIds;

    @ApiModelProperty("工程边界")
    private String boundary;
}
