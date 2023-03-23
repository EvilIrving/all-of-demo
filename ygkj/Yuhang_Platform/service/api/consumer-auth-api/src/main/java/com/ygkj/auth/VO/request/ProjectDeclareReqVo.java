package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: fml
 * @Date: 2020/9/16 15:02
 * @Description:
 */
@Data
public class ProjectDeclareReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事项id")
    //@NotBlank
    private String matterId;

    @ApiModelProperty("单位性质")
    private String unitProperty;

    @ApiModelProperty("法人代表")
    private String legalRepresentative;

    @ApiModelProperty("法人电话")
    private String corporatePhone;

    @ApiModelProperty("邮政编码")
    private String zipCode;

    @ApiModelProperty("行政区划")
    private String areaName;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("单位地址")
    private String unitAddress;

    @ApiModelProperty("主管部门")
    private String competentDepartment;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("项目类型")
    private String projectType;

    @ApiModelProperty("项目性质")
    private String projectProperty;

    @ApiModelProperty("建设位置")
    private String buildLocation;

    @ApiModelProperty("设计规模")
    private String designScale;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    @ApiModelProperty("建筑物孔数(孔)")
    private Integer buildHoles;

    @ApiModelProperty("净宽")
    private Double clearWidth;

    @ApiModelProperty("梁底高程(孔)")
    private Double bottomElevation;

    @ApiModelProperty("河道宽度(米)")
    private Double riverWidth;

    @ApiModelProperty("设计洪水标准(%)")
    private Double designFloodStandard;

    @ApiModelProperty("流量(米³/秒)")
    private Double flow;

    @ApiModelProperty("建成后抬高水位(米)")
    private Double raiseWaterLevel;

    @ApiModelProperty("通航标准(级)")
    private Double navigationStandar;

    @ApiModelProperty("轴线与主流交角")
    private Double intersectionAngleAxis;

    @ApiModelProperty("其他事项（跨河工程）")
    private String rivercrossOtherMatters;

    @ApiModelProperty("延安线长度(米)")
    private Double yananLength;

    @ApiModelProperty("建筑面积(米²)")
    private Double riversideBuildArea;

    @ApiModelProperty("建设内容")
    private String buildContent;

    @ApiModelProperty("其他事项（临河工程）")
    private String riversideOtherMatters;

    @ApiModelProperty("建筑物顶高程(穿河工程)")
    private Double riverstrideBuidingTopElevation;

    @ApiModelProperty("建筑面积(米²)")
    private Double riverstrideBuildArea;

    @ApiModelProperty("建筑物断面积(米)")
    private Double riverstrideSectionalArea;

    @ApiModelProperty("其他事项（穿河工程）")
    private String riverstrideOtherMatters;

    @ApiModelProperty("建筑物类别(穿堤工程)")
    private String dikecrossBuildType;

    @ApiModelProperty("建筑物尺寸(穿堤工程)")
    private Double dikecrossBuildSize;

    @ApiModelProperty("穿堤部位(穿堤工程)")
    private String dikecrossPart;

    @ApiModelProperty("其它事项穿堤工程)")
    private String dikecrossOtherMatters;

    @ApiModelProperty("取水流量")
    private Double waterintakeTakeflow;

    @ApiModelProperty("水质要求")
    private String waterintakeQualityRequire;

    @ApiModelProperty("取水方式")
    private String waterintakeTakeType;

    @ApiModelProperty("取水口水位正常")
    private Double waterintakeWaterlevelNormal;

    @ApiModelProperty("取水口水位最高")
    private Double waterintakeWaterlevelHighest;

    @ApiModelProperty("取水口水位最低")
    private Double waterintakeWaterlevelMinimum;

    @ApiModelProperty("取水口建筑物形式")
    private String waterintakeBuildType;

    @ApiModelProperty("其他事项（取水口）")
    private String waterintakeOtherMatters;

    @ApiModelProperty("排污量(吨/日)")
    private Double waterouttakeDischargeCapacityTotal;

    @ApiModelProperty("排污量(吨/日)")
    private Double waterouttakeDischargeCapacityClean;

    @ApiModelProperty("排污量(吨/日)")
    private Double waterouttakeDischargeCapacitySewage;

    @ApiModelProperty("主要污染物质")
    private String waterouttakeMainPollutants;

    @ApiModelProperty("建筑物形式(出水口)")
    private String waterouttakeBuildingType;

    @ApiModelProperty("其他事项（出水口）")
    private String waterouttakeOtherMatters;

    @ApiModelProperty("占用长度(米)(占用水域)")
    private Double waterOccupyLength;

    @ApiModelProperty("占用面积(占用水域)")
    private Double waterOccupyArea;

    @ApiModelProperty("占用用途(占用水域)")
    private String waterOccupyUse;

    @ApiModelProperty("其他事项(占用水域)")
    private String waterOccupyOther;

    @ApiModelProperty("占用经度(占用水域)")
    private Double waterOccupyLng;

    @ApiModelProperty("占用纬度(占用水域)")
    private Double waterOccupyLat;

    @ApiModelProperty("卫星定位图(占用水域)")
    private String waterOccupyMapImg;

    @ApiModelProperty("规划红线图(占用水域)")
    private String waterOccupyPlanImg;

    @ApiModelProperty("勘测定界图(占用水域)")
    private String waterOccupyDelimitImg;

    @ApiModelProperty("开挖深度(米)(开挖工程)")
    private Double excavateDepth;

    @ApiModelProperty("开挖面积(米²)(开挖工程)")
    private Double excavateArea;

    @ApiModelProperty("地点(开挖工程)")
    private String excavatePoint;

    @ApiModelProperty("卫星定位图(开挖工程)")
    private String excavateMapImg;

    @ApiModelProperty("规划红线图(开挖工程)")
    private String excavatePlanImg;

    @ApiModelProperty("勘测定界图(开挖工程)")
    private String excavateDelimitImg;

    @ApiModelProperty("其他事项(开挖工程)")
    private String excavateOther;

    @ApiModelProperty("有关事项")
    private String relatedMatters;

    @ApiModelProperty("申请理由")
    private String applyReason;

    @ApiModelProperty("范围点位集合)")
    private String linePoint;

    /*@ApiModelProperty("申请材料")
    private String applyData;*/

    @ApiModelProperty("申请材料")
    private List<ProjectFileReqVO> projectFileReqVOS;
}
