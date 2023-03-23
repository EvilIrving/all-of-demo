package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:24
 * @description 泵站基础信息表
 */
@Data
@ApiModel("泵站基础信息表 ")
public class AttPustBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("泵站代码")
    private String pustCode;


    @ApiModelProperty("泵站名称")
    private String pustName;


    @ApiModelProperty("泵站经度")
    private Double pustLong;


    @ApiModelProperty("泵站维度")
    private Double pustLat;


    @ApiModelProperty("泵站水闸位置")
    private String pustLoc;


    @ApiModelProperty("泵站类型")
    private String pustType;


    @ApiModelProperty("装机流量")
    private Double insFlow;


    @ApiModelProperty("装机功率")
    private Double insPow;


    @ApiModelProperty("水泵数量")
    private Integer pumpNum;


    @ApiModelProperty("设计扬程")
    private Double desHead;


    @ApiModelProperty("工程任务")
    private String engTask;


    @ApiModelProperty("工程等别")
    private String engGrad;


    @ApiModelProperty("工程规模")
    private String engScal;


    @ApiModelProperty("主要建筑物级别")
    private String mainBuildGrad;


    @ApiModelProperty("工程建设情况")
    private String engStart;


    @ApiModelProperty("开工时间")
    private String startDate;


    @ApiModelProperty("建成时间")
    private String builtdate;


    @ApiModelProperty("竣工验收时间")
    private String compDate;


    @ApiModelProperty("归口管理部门")
    private String admDep;


    @ApiModelProperty("是否通过标准化管理创建")
    private String staMan;


    @ApiModelProperty("主要构筑物")
    private String mainbuilding;


    @ApiModelProperty("是否竣工验收 （非加固）")
    private String isComplete;


    @ApiModelProperty("是否为闸站工程")
    private String isGateStationProject;


    @ApiModelProperty("泵站参照水位站编码")
    private String pumpReferenceWlStationCode;


    @ApiModelProperty("泵站参照水位站名称")
    private String pumpReferenceWlStationName;


    @ApiModelProperty("泵站参照水位站警戒水位")
    private Double pumpReferenceWlStationWarningLevel;


    @ApiModelProperty("泵站参照水位站危急水位")
    private Double pumpReferenceWlStationCriticalLevel;


    @ApiModelProperty("内河常水位(米)")
    private Double riverWaterLevel;


    @ApiModelProperty("电动机台数(台)")
    private Integer motorNumber;


    @ApiModelProperty("备注")
    private String note;


    @ApiModelProperty("记录生效时间")
    private String effDate;


    @ApiModelProperty("记录失效时间")
    private String exprDate;


    @ApiModelProperty("行政区划代码")
    private String areaCode;


    @ApiModelProperty("政府负责人用户id，关联到用户体系")
    private String govermentChargeId;


    @ApiModelProperty("水行政部门负责人用户id，关联到用户体系")
    private String waterDepartmentChargeId;


    @ApiModelProperty("主管部门负责人id，关联到用户体系")
    private String competentDepartmentChargeId;


    @ApiModelProperty("管理单位责任人id，关联到用户体系")
    private String manageDepartmentChargeId;


    @ApiModelProperty("技术负责人")
    private String technologyChargeId;


    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;


    @ApiModelProperty("所属流域")
    private String bas;


    @ApiModelProperty("设计标准")
    private String designStandard;
}
