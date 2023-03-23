package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fml
 * @date 2021-03-29 15:27
 * @description 水闸基础信息表
 */
@Data
@ApiModel("水闸基础信息表 ")
public class AttWagaBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水闸代码")
    private String wagaCode;


    @ApiModelProperty("水闸名称")
    private String wagaName;


    @ApiModelProperty("起点经度")
    private Double startLong;


    @ApiModelProperty("起点维度")
    private Double startLat;


    @ApiModelProperty("终点经度")
    private Double endLong;


    @ApiModelProperty("终点维度")
    private Double endLat;


    @ApiModelProperty("水闸所在位置")
    private String wagaLoc;


    @ApiModelProperty("水闸类型")
    private String wagaType;


    @ApiModelProperty("水闸用途")
    private String wagaUse;


    @ApiModelProperty("工程等别")
    private String engGrad;


    @ApiModelProperty("工程规模")
    private String engScal;


    @ApiModelProperty("主要建筑物级别")
    private String mainBuildGrad;


    @ApiModelProperty("设计最大过闸 流量")
    private Double desLockDisc;


    @ApiModelProperty("闸口数量")
    private Integer gaorNum;


    @ApiModelProperty("闸口总净宽")
    private Double gaorTotNetWid;


    @ApiModelProperty("孔口断面形式")
    private String op;


    @ApiModelProperty("孔口净高")
    private Double oh;


    @ApiModelProperty("孔口内径")
    private Double od;


    @ApiModelProperty("工程建设情况")
    private String engStat;


    @ApiModelProperty("开工时间")
    private String startDate;


    @ApiModelProperty("竣工验收时间")
    private String compDate;


    @ApiModelProperty("归口管理部门")
    private String amdDep;


    @ApiModelProperty("建成时间")
    private String builtdate;


    @ApiModelProperty("闸门宽度")
    private String gatewidth;


    @ApiModelProperty("闸底高度")
    private Double gatebottomelevation;


    @ApiModelProperty("里程")
    private Integer mileage;


    @ApiModelProperty("是否竣工验收")
    private String isComplete;


    @ApiModelProperty("是否为闸站工程")
    private String isGateStationProject;


    @ApiModelProperty("是否为套闸工程")
    private String isSetBrakeEngineering;


    @ApiModelProperty("闸上是否能通车")
    private String isTraffic;


    @ApiModelProperty("水闸参照水位站名称")
    private String referenceWlStationName;


    @ApiModelProperty("水闸参照水位站站号")
    private String referenceWlStationCode;


    @ApiModelProperty("水闸参照水位站警戒水位")
    private Double referenceWlStationWarningLevel;


    @ApiModelProperty("水闸参照水位站危急水位")
    private Double referenceWlStationCriticalLevel;


    @ApiModelProperty("高程体系")
    private String elevationSystem;


    @ApiModelProperty("闸门形式")
    private String gateType;


    @ApiModelProperty("闸门高度")
    private String gateSize;


    @ApiModelProperty("启闭机类型")
    private String hoistType;


    @ApiModelProperty("效能方式")
    private String energyDissipationType;


    @ApiModelProperty("节制闸过闸流量(立方米/秒)")
    private Double controlGateFlow;


    @ApiModelProperty("设计闸上水位(米)")
    private Double designSluiceUpstreamLevel;


    @ApiModelProperty("校核闸上水位(米)")
    private Double checkSluiceUpstreamLevel;


    @ApiModelProperty("上下游最大水位差(米)")
    private Double biggestWaterLevelDifferenc;


    @ApiModelProperty("正常蓄水位(米)")
    private Double normalImpoundageLevel;


    @ApiModelProperty("梅汛期内河控制水位(米)")
    private Double meiyufloodPeriodWaterLevel;


    @ApiModelProperty("台汛期内河控制水位(米)")
    private Double typhoonPeriodWaterLevel;


    @ApiModelProperty("属河道水闸或海塘水闸")
    private String isRiverSluice;


    @ApiModelProperty("是否五水共治任务")
    private String isWsgz;


    @ApiModelProperty("所在河道等级")
    private String riverLevel;


    @ApiModelProperty("所在河流岸别")
    private String bankSide;


    @ApiModelProperty("是否有备用电源")
    private String hasEmergencyPower;


    @ApiModelProperty("是否船闸工程")
    private String isShipLock;


    @ApiModelProperty("闸基处理方式")
    private String basePattern;


    @ApiModelProperty("静止模式")
    private String stillingPattern;


    @ApiModelProperty("设计洪水")
    private Integer designflood;


    @ApiModelProperty("校核洪水")
    private Integer checkflood;


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

    /**
     * 主测站编码，水闸本来没有主测站一说，但是目前数据库中有重复数据，一个测站可能关联多个工程。如果主测站为空，则查询att_st_base.proj_code
     */
    @ApiModelProperty("主测站编码")
    private String mainStcd;

    @ApiModelProperty("河道正常蓄水位")
    private BigDecimal riverNormalPoolLevel;

    @ApiModelProperty("死水位")
    private BigDecimal dealLevel;

    @ApiModelProperty("非汛期控制水位")
    private BigDecimal nonFloodPeriodWaterLevel;

    @ApiModelProperty("兴利控制水位")
    private BigDecimal hennessyPeriodWaterLevel;

    @ApiModelProperty("兴利死水位")
    private BigDecimal hennessyDealLevel;

    @ApiModelProperty("安全水位上限")
    private BigDecimal safeLevelUpper;

    @ApiModelProperty("安全水位下限")
    private BigDecimal safeLevelLower;
}
