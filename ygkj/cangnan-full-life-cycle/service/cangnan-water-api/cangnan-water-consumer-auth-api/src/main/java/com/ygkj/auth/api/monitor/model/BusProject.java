package com.ygkj.auth.api.monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author huangzh
 * @description 审批-待办项目
 * @date 2020-09-08
 */
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class BusProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 事项id
     */
    private String matterId;

    /**
     * 单位性质
     */
    private String unitProperty;

    /**
     * 法人代表
     */
    private String legalRepresentative;

    /**
     * 法人电话
     */
    private String corporatePhone;

    /**
     * 邮政编码
     */
    private String zipCode;

    /**
     * 行政区划
     */
    private String areaName;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位地址
     */
    private String unitAddress;

    /**
     * 主管部门
     */
    private String competentDepartment;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 项目性质
     */
    private String projectProperty;

    /**
     * 建设位置
     */
    private String buildLocation;

    /**
     * 设计规模
     */
    private String designScale;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 建筑物孔数(孔)
     */
    private Integer buildHoles;

    /**
     * 净宽
     */
    private Double clearWidth;

    /**
     * 梁底高程(孔)
     */
    private Double bottomElevation;

    /**
     * 河道宽度(米)
     */
    private Double riverWidth;

    /**
     * 设计洪水标准(%)
     */
    private Double designFloodStandard;

    /**
     * 流量(米³/秒)
     */
    private Double flow;

    /**
     * 建成后抬高水位(米)
     */
    private Double raiseWaterLevel;

    /**
     * 通航标准(级)
     */
    private Double navigationStandar;

    /**
     * 轴线与主流交角
     */
    private Double intersectionAngleAxis;

    /**
     * 其他事项（跨河工程）
     */
    private String rivercrossOtherMatters;

    /**
     * 延安线长度(米)
     */
    private Double yananLength;

    /**
     * 建筑面积(米²)
     */
    private Double riversideBuildArea;

    /**
     * 建设内容
     */
    private String buildContent;

    /**
     * 其他事项（临河工程）
     */
    private String riversideOtherMatters;

    /**
     * 建筑物顶高程(穿河工程)
     */
    private Double riverstrideBuidingTopElevation;

    /**
     * 建筑面积(米²)
     */
    private Double riverstrideBuildArea;

    /**
     * 建筑物断面积(米)
     */
    private Double riverstrideSectionalArea;

    /**
     * 其他事项（穿河工程）
     */
    private String riverstrideOtherMatters;

    /**
     * 建筑物类别(穿堤工程)
     */
    private String dikecrossBuildType;

    /**
     * 建筑物尺寸(穿堤工程)
     */
    private Double dikecrossBuildSize;

    /**
     * 穿堤部位(穿堤工程)
     */
    private String dikecrossPart;

    /**
     * 其它事项(穿堤工程)
     */
    private String dikecrossOtherMatters;

    /**
     * 取水流量
     */
    private Double waterintakeTakeflow;

    /**
     * 水质要求
     */
    private String waterintakeQualityRequire;

    /**
     * 取水方式
     */
    private String waterintakeTakeType;

    /**
     * 取水口水位正常
     */
    private Double waterintakeWaterlevelNormal;

    /**
     * 取水口水位最高
     */
    private Double waterintakeWaterlevelHighest;

    /**
     * 取水口水位最低
     */
    private Double waterintakeWaterlevelMinimum;

    /**
     * 取水口建筑物形式
     */
    private String waterintakeBuildType;

    /**
     * 其他事项（取水口）
     */
    private String waterintakeOtherMatters;

    /**
     * 排污量(吨/日)
     */
    private Double waterouttakeDischargeCapacityTotal;

    /**
     * 排污量(吨/日)
     */
    private Double waterouttakeDischargeCapacityClean;

    /**
     * 排污量(吨/日)
     */
    private Double waterouttakeDischargeCapacitySewage;

    /**
     * 主要污染物质
     */
    private String waterouttakeMainPollutants;

    /**
     * 建筑物形式(出水口)
     */
    private String waterouttakeBuildingType;

    /**
     * 其他事项（出水口）
     */
    private String waterouttakeOtherMatters;

    /**
     * 占用长度(米)(占用水域)
     */
    private Double waterOccupyLength;

    /**
     * 占用面积(占用水域)
     */
    private Double waterOccupyArea;

    /**
     * 占用用途(占用水域)
     */
    private String waterOccupyUse;

    /**
     * 其他事项(占用水域)
     */
    private String waterOccupyOther;

    /**
     * 占用经度(占用水域)
     */
    private Double waterOccupyLng;

    /**
     * 占用纬度(占用水域)
     */
    private Double waterOccupyLat;

    /**
     * 卫星定位图(占用水域)
     */
    private String waterOccupyMapImg;

    /**
     * 规划红线图(占用水域)
     */
    private String waterOccupyPlanImg;

    /**
     * 勘测定界图(占用水域)
     */
    private String waterOccupyDelimitImg;

    /**
     * 开挖深度(米)(开挖工程)
     */
    private Double excavateDepth;

    /**
     * 开挖面积(米²)(开挖工程)
     */
    private Double excavateArea;

    /**
     * 地点(开挖工程)
     */
    private String excavatePoint;

    /**
     * 卫星定位图(开挖工程)
     */
    private String excavateMapImg;

    /**
     * 规划红线图(开挖工程)
     */
    private String excavatePlanImg;

    /**
     * 勘测定界图(开挖工程)
     */
    private String excavateDelimitImg;

    /**
     * 其他事项(开挖工程)
     */
    private String excavateOther;

    /**
     * 有关事项
     */
    private String relatedMatters;

    /**
     * 申请理由
     */
    private String applyReason;

    /**
     * 申请材料
     */
    /*private String applyData;*/

    /**
     * 申报时间
     */
    private Date reportDate;

    /**
     * 申报状态(1已申报，0未申报)
     */
    private Integer reportStatus;

    /**
     * 审批状态(1受理中(待受理)，2不受理，3待补正(补正中)，4审核中(待审核)，5签批中(待签批)，6待校对(校对中)，7已校对，8已办结)
     */
    private Integer processStatus;

    /**
     * 结束时间
     */
    private Date processEndTime;

    /**
     * 范围点位集合
     */
    private String linePoint;

    /**
     * 申请人所在单位
     */
    private String deptId;

    /**
     * 标签
     */
    private String tagId;

    /**
     * 标签信息
     */
    private BusProjectTag busProjectTag;

    /**
     * 申请材料
     */
    private List<BusProjectFile> busProjectFiles;

}
